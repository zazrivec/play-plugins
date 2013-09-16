package com.typesafe.plugin

import sbt._
import sbt.PlayExceptions.AssetCompilationException
import java.io.InputStreamReader
import scala.Some
import sbt.PlayExceptions.AssetCompilationException
import org.apache.commons.io.FilenameUtils

object DustCompiler {

  import org.mozilla.javascript._
  import org.mozilla.javascript.tools.shell._

  import scala.collection.JavaConverters._

  import scalax.file.
  _
  def compile(name: String, source: java.io.File, nativePath: Option[String] = None): String = {
    try {
      nativePath match {
        case Some(nativeCompiler) if (new java.io.File(nativeCompiler).exists) => compileNative(name, source, nativeCompiler)
        case _ => compiler(name, IO.read(source))
      }
    } catch {
      case e: JavaScriptException => {

        val line = ".* At line : (\\d+), column : (\\d+)".r
        val error = e.getValue.asInstanceOf[Scriptable]

        throw ScriptableObject.getProperty(error, "message").asInstanceOf[String] match {
          case msg @ line(l) => AssetCompilationException(Some(source), msg, Some(Integer.parseInt(l)), None)
          case msg => AssetCompilationException(Some(source), msg, None, None)
        }
      }
    }
  }

  private lazy val compiler = {

    val ctx = Context.enter; ctx.setOptimizationLevel(-1)
    val global = new Global; global.init(ctx)
    val scope = ctx.initStandardObjects(global)

    val wrappedDustCompiler = Context.javaToJS(this, scope)
    ScriptableObject.putProperty(scope, "DustCompiler", wrappedDustCompiler)

    ctx.evaluateReader(
      scope, new InputStreamReader(
        this.getClass.getClassLoader.getResource("dust-full-2.0.3.js").openConnection().getInputStream()),
      "dust-full.js",
      1, null)

    val dust = scope.get("dust", scope).asInstanceOf[NativeObject]
    val compilerFunction = dust.get("compile", scope).asInstanceOf[Function]

    Context.exit

    (name: String, source: String) => {
      Context.call(null, compilerFunction, scope, scope, Array(source, name)).asInstanceOf[String]
    }
  }

  def compileNative(name: String, source: File, nativePath: String): String = {
    import scala.sys.process._
    val pb = Process(nativePath + " --name=" + name + " " + source.getPath)
    var out = List[String]()
    var err = List[String]()
    val exit = pb ! ProcessLogger((s) => out ::= s, (s) => err ::= s)
    if (exit != 0) {
      val message = err.mkString("");

      val DustCompileError = ".* At line : (\\d+), column : (\\d+)".r

      message match {
        case DustCompileError(line, column) => throw AssetCompilationException(Some(source), message, Some(line.toInt), Some(column.toInt))
        case _ => throw AssetCompilationException(Some(source), message, None, None) // Some other weird error, we have no line/column info now.
      }
    }
    out.reverse.mkString("\n")
  }

}
