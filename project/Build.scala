import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "cfpApp"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(

    "securesocial" %% "securesocial" % "master-SNAPSHOT",
    "com.novus" %% "salat" % "1.9.2-SNAPSHOT",

      // Add your project dependencies here,
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalaVersion := "2.10.0",
    resolvers += "Repo 1" at "http://repo1.maven.org/maven2/",
    resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
    resolvers += "repo.novus rels" at "http://repo.novus.com/releases/",
    resolvers += "Scalatools repo" at "http://oss.sonatype.org/content/groups/scala-tools/",
    resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    resolvers += "Code hale" at "http://repo.codahale.com/",
    resolvers += Resolver.url("sbt-plugin-snapshots", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots/"))(Resolver.ivyStylePatterns)
    // Add your own project settings here      
  )

}
