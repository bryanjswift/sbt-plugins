package bjs.jackrabbit

import sbt._

trait CleanRepositoryPlugin extends Project with io.DeleteDirectoryPlugin {
	def repositoryPath = "." / "repository"
	// Define task to clean up repository from filesystem
	lazy val cleanRepository = task {
		try {
			repositoryPath.getFiles.foreach(deleteDirectory)
			None
		} catch {
			case e:Exception =>
				Some(e.getMessage)
		}
	}
}

