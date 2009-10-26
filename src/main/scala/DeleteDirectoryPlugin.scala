package bjs.io

import sbt._

trait DeleteDirectoryPlugin extends Project {
	def deleteDirectory(path:java.io.File):Boolean = {
		path.listFiles.foreach(f => if (f.isDirectory) deleteDirectory(f) else f.delete)
		path.delete
	}
}
