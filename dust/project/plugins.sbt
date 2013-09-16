// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases",
  "fuzion24-releases" at "http://fuzion24.github.io/maven/releases"
)

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.2.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.6.2")

addSbtPlugin("com.github.hexx" % "sbt-github-repo" % "0.1.0")