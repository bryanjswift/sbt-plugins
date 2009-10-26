package bjs.io

import sbt._
import Process._

trait LessCompilerPlugin extends BasicWebScalaProject with MavenStyleWebScalaPaths {
	def lessCompiler = propertyOptional[String]("lessc")
	def lessFiles = webappPath ** "*.less"
	// if lessc is on $PATH, call it on any outdated .less files in webappPath
	def lessc(sources:PathFinder):Task = {
		val products = for (path <- sources.get) yield Path.fromString(".",path.toString.replaceAll("less$","css"))
		fileTask("less", products from sources) {
			try {
				val paths = lessFiles.getPaths
				for (path <- paths) { (lessCompiler.value + " " + path) ! log }
				None
			} catch {
				case e:Exception => Some(e.getMessage)
			}
		}
	}
	lazy val lessCompile = lessc(lessFiles)
	// override webapp resources to filter out .less files
	override def webappResources = super.webappResources --- lessFiles
	// modify the prepareWebappAction to compile .less files
	override def prepareWebappAction = super.prepareWebappAction dependsOn(lessCompile)
}

