package bjs.webapp

import sbt._

trait CleanWebappPlugin extends MavenStyleWebScalaPaths {
	def webappClassesDirectory = webappPath / "WEB-INF" / "classes"
	def webappLibDirectory = webappPath / "WEB-INF" / "lib"
	// Define task to delete webapp class and jar files
	lazy val cleanWebapp = task {
		val toClean = webappClassesDirectory ** "*" +++ webappLibDirectory * "*.jar"
		try {
			toClean.getFiles.foreach(_.delete)
			None
		} catch {
			case e:Exception => 
				Some(e.getMessage)
		}
	}
}

