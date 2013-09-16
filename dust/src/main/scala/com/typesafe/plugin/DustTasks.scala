package com.typesafe.plugin

import java.io.{ File, FileInputStream, InputStreamReader }
import org.apache.commons.io.FilenameUtils
import org.mozilla.javascript.tools.shell.Global
import org.mozilla.javascript.{ Context, JavaScriptException, Scriptable, ScriptableObject}

import sbt._
import sbt.PlayExceptions.AssetCompilationException

trait DustTasks extends DustKeys {

  import Keys._

  protected def templateName(sourceFile: String, assetsDir: String): String = {
    val sourceFileWithForwardSlashes = FilenameUtils.separatorsToUnix(sourceFile)
    val assetsDirWithForwardSlashes  = FilenameUtils.separatorsToUnix(assetsDir)
    FilenameUtils.removeExtension(
      sourceFileWithForwardSlashes.replace(assetsDirWithForwardSlashes + "/", "")
    )
  }

  lazy val dustCompiler = (sourceDirectory in Compile, resourceManaged in Compile, cacheDirectory, dustFileRegexFrom, dustFileRegexTo, dustAssetsDir, dustAssetsGlob, dustOutputRelativePath, dustNativePath) map {
    (src, resources, cache, fileReplaceRegexp, fileReplaceWith, assetsDir, files, outputRelativePath, nativePath) =>
      
      val cacheFile = cache / "dust"

      def naming(name: String) = name.replaceAll(fileReplaceRegexp, fileReplaceWith)

      val currentInfos = files.get.map(f => f -> FileInfo.lastModified(f)).toMap

      val (previousRelation, previousInfo) = Sync.readInfo(cacheFile)(FileInfo.lastModified.format)
      val previousGeneratedFiles = previousRelation._2s
      
      if (previousInfo != currentInfos) {

        previousGeneratedFiles.foreach(IO.delete)

        val generated = (files x relativeTo(assetsDir)).flatMap {
          case (sourceFile, name) => {
            val msg = DustCompiler.compile(templateName(sourceFile.getPath, assetsDir.getPath), sourceFile, nativePath)
            val out = new File(resources, "public/" + outputRelativePath + naming(name))
            IO.write(out, msg)
            Seq(sourceFile -> out)
          }
        }

        Sync.writeInfo(cacheFile,
          Relation.empty[java.io.File, java.io.File] ++ generated,
          currentInfos)(FileInfo.lastModified.format)

        generated.map(_._2).distinct.toList
      } else {
        previousGeneratedFiles.toSeq
      }
  }
}