import org.specs2.mutable._
import org.specs2.specification._

import com.typesafe.plugin._
import java.io.File

class DustTasksSpec extends Specification with DustTasks {
  trait testData extends Scope {
    val output = """(function(){dust.register("test",body_0);function body_0(chk,ctx){return chk.write("Hello ").reference(ctx.get("name"),ctx,"h").write("! You have ").reference(ctx.get("count"),ctx,"h").write(" new messages.");}return body_0;})();"""
    val complex = """(function(){dust.register("fragment",body_0);function body_0(chk,ctx){return chk.write("<div class=\"row margin-bottom-10\"><div class=\"col-md-12 activity\">{").section(ctx.get("propagation"),ctx,{"block":body_1},null).write("}{").notexists(ctx.get("propagation"),ctx,{"block":body_2},null).write("}<div class=\"row\"><div class=\"col-md-8 activity\"><div class=\"row small-gallery\">{").section(ctx.get("files"),ctx,{"block":body_5},null).write("}<\/div><\/div><div class=\"col-md-4\">{").section(ctx.getPath(false,["tags","Program"]),ctx,{"block":body_6},null).write("}<\/div><\/div><div class=\"row\"><div class=\"col-md-8\"><span class=\"expandable\">{").reference(ctx.get("description"),ctx,"h").write("}<\/span><\/div><\/div><div class=\"row\"><div class=\"col-md-12\"><p><span class=\"glyphicon glyphicon-calendar\"><\/span>{").reference(ctx.get("fromYMD"),ctx,"h").write("}{").section(ctx.get("toYMD"),ctx,{"block":body_7},null).write("}{").section(ctx.get("canEdit"),ctx,{"else":body_8,"block":body_10},null).write("}<span>{").reference(ctx.get("shareText"),ctx,"h").write("}&nbsp;<\/span><a class=\"share btn btn-xs btn-facebook\" data-id=\"{").reference(ctx.get("id"),ctx,"h").write("}\" data-href=\"https:\/\/www.facebook.com\/sharer\/sharer.php?u=\" target=\"_blank\"><span class=\"icon-facebook\"><\/span> | facebook<\/a>&nbsp;<a class=\"share btn btn-xs btn-twitter\" data-id=\"{").reference(ctx.get("id"),ctx,"h").write("}\" data-href=\"https:\/\/twitter.com\/share?url=\" target=\"_blank\"><span class=\"icon-twitter\"><\/span> | twitter<\/a>&nbsp;<a class=\"share btn btn-xs btn-google-plus\" data-id=\"{").reference(ctx.get("id"),ctx,"h").write("}\" data-href=\"https:\/\/plus.google.com\/share?url=\" target=\"_blank\"><span class=\"icon-google-plus\"><\/span> | google<\/a>&nbsp;<\/p><\/div><\/div><\/div><\/div>");}function body_1(chk,ctx){return chk.write("}<div class=\"panel panel-warning\"><div class=\"panel-heading\">Propagácia<\/div><\/div>{");}function body_2(chk,ctx){return chk.write("}{").section(ctx.get("invitation"),ctx,{"block":body_3},null).write("}{").notexists(ctx.get("invitation"),ctx,{"block":body_4},null).write("}{");}function body_3(chk,ctx){return chk.write("}<div class=\"panel panel-danger\"><div class=\"panel-heading\">Pozvánka<\/div><\/div>{");}function body_4(chk,ctx){return chk.write("}<div class=\"panel panel-info\"><div class=\"panel-heading\">Realizovaná aktivita<\/div><\/div>{");}function body_5(chk,ctx){return chk.write("}<a class=\"fancybox\" data-fancybox-group=\"activity-{").reference(ctx.get("activityId"),ctx,"h").write("}\" href=\"{").reference(ctx.get("img"),ctx,"h").write("}\" title=\"Fotky z aktivity {").reference(ctx.get("shortDescription"),ctx,"h").write("}\"><img data-src=\"{").reference(ctx.get("imgThumbnail"),ctx,"h").write("}\" class=\"img-rounded\" alt=\"Fotky z aktivity {").reference(ctx.get("altDescription"),ctx,"h").write("}\"><\/a>{");}function body_6(chk,ctx){return chk.write("}<div>Program: <a href=\"{").reference(ctx.get("programUrl"),ctx,"h").write("}\">{").reference(ctx.getPath(false,["tags","Program"]),ctx,"h").write("}<\/a><\/div><div>Organizácia: {").reference(ctx.getPath(false,["tags","Organization"]),ctx,"h").write("}<\/div><div>Projekt: <a href=\"{").reference(ctx.get("projectUrl"),ctx,"h").write("}\">{").reference(ctx.getPath(false,["tags","Project"]),ctx,"h").write("}<\/a><\/div><div>Mesto: {").reference(ctx.getPath(false,["tags","City"]),ctx,"h").write("}<\/div>{");}function body_7(chk,ctx){return chk.write("}- {").reference(ctx.get("toYMD"),ctx,"h").write("}{");}function body_8(chk,ctx){return chk.write("{").section(ctx.get("loggedIn"),ctx,{"block":body_9},null).write("}{");}function body_9(chk,ctx){return chk.write("}{{").reference(ctx.get("editElement"),ctx,"h").write("}}{");}function body_10(chk,ctx){return chk.write("}<span class=\"glyphicon glyphicon-edit\"><\/span><a href=\"{").reference(ctx.get("editUrl"),ctx,"h").write("}\">&nbsp;Upraviť &raquo;<\/a>");}return body_0;})();"""
  }
  
  "The Dust compiler" should {
    "resolve template names correctly on Unix" in {
      val file = "/var/projects/sample/app/assets/templates/foo.tl"
      val assetsDir = "/var/projects/sample/app/assets"
      templateName(file, assetsDir) must be_==("templates/foo")
    }
    "resolve template names correctly on Windows" in {
      val file = "C:\\projects\\sample\\app\\assets\\templates\\foo.tl"
      val assetsDir = "C:\\projects\\sample\\app\\assets"
      templateName(file, assetsDir) must be_==("templates/foo")
    }
    "use embedded compiler by default" in new testData {
      val msg = DustCompiler.compile("test", new java.io.File(getClass.getResource("/test.tl").toURI))
      msg === output
    }
    "defer to native compiler if one is specified and located" in new testData {
      val msg = DustCompiler.compile("test", new java.io.File(getClass.getResource("/test.tl").toURI),
        Some("/usr/local/share/npm/bin/dustc"))
      msg === output
    }
    "revert to embedded compiler if native one is specified but not located" in new testData {
      val msg = DustCompiler.compile("test", new java.io.File(getClass.getResource("/test.tl").toURI),
        Some("a/road/to/nowhere/"))
      msg === output
    }
    "use embedded compiler by default for complex templateS" in new testData {
      val msg = DustCompiler.compile("fragment", new java.io.File(getClass.getResource("/fragment.tl").toURI))
      msg === complex
    }
  }
}