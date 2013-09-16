import sbt.Defaults._

seq(githubRepoSettings: _*)

sbtPlugin := true

name := "play-plugins-dust"

version := "1.6"

organization := "com.typesafe"

scalacOptions ++= Seq("-deprecation")

addSbtPlugin("play" % "sbt-plugin" % "2.1.4")

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.4",
  "org.specs2" %% "specs2" % "1.12.3" % "test"
)

publishMavenStyle := true

localRepo := Path.userHome / "github" / "repo"

githubRepo := "git@github.com:zazrivec/maven.git"

//publishTo <<= (version) { version: String =>
//  val typesafeIvyReleases = Resolver.url("Typesafe Ivy Releases Repository", url("http://typesafe.artifactoryonline.com/typesafe/ivy-releases/"))(Resolver.ivyStylePatterns)
//  val typesafeIvySnapshot = Resolver.url("Typesafe Ivy Snapshots Repository", url("http://typesafe.artifactoryonline.com/typesafe/ivy-snapshots/"))(Resolver.ivyStylePatterns)
//  val repo =  if (version.trim.endsWith("SNAPSHOT")) typesafeIvySnapshot
//                      else typesafeIvyReleases
//  Some(repo)
//}