import sbt._

class SbtPluginProject(info:ProjectInfo) extends PluginProject(info) {
	override def managedStyle = ManagedStyle.Ivy
	val publishTo = Resolver.sftp("Bryan J Swift","repos.bryanjswift.com",3748,"ivy2/")
	Credentials(Path.userHome / ".ivy2" / ".credentials", log)
	override def packageDocsJar = defaultJarPath("-javadoc.jar")
	override def packageSrcJar= defaultJarPath("-sources.jar")
	val sourceArtifact = Artifact(artifactID, "src", "jar", Some("sources"), Nil, None)
	val docsArtifact = Artifact(artifactID, "docs", "jar", Some("javadoc"), Nil, None)
	override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageDocs, packageSrc)
}
