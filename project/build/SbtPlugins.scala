import sbt._

class SbtPluginProject(info:ProjectInfo) extends PluginProject(info) {
	// Publish settings
	override def managedStyle = ManagedStyle.Maven
	val publishTo = Resolver.sftp("Bryan J Swift","repos.bryanjswift.com",3748,"/var/www/vhosts/repos.bryanjswift.com/public/maven2/")
	// Also package sources and docs
	override def packageDocsJar = defaultJarPath("-javadoc.jar")
	override def packageSrcJar= defaultJarPath("-sources.jar")
	val sourceArtifact = Artifact(artifactID, "src", "jar", Some("sources"), Nil, None)
	val docsArtifact = Artifact(artifactID, "docs", "jar", Some("javadoc"), Nil, None)
	override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageDocs, packageSrc)
}
