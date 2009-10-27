import sbt._

class SbtPluginProject(info:ProjectInfo) extends PluginProject(info) {
	// *-- Copy of ResolverPlugin contents because a project can't depend on itself
	val resolverPath = propertyOptional[String](".resolver")
	private def sbtPublishResolver = {
		import java.io.FileInputStream
		import java.util.Properties
		val props = new Properties() { def apply(key:String) = this.getProperty(key) }
		props.load(new FileInputStream(resolverPath.value))
		props("resolver.type") match {
			case "sftp" => Resolver.sftp(props("resolver.name"),props("resolver.host"),props("resolver.port").toInt,props("resolver.path"))
			case "ssh" => Resolver.ssh(props("resolver.name"),props("resolver.host"),props("resolver.port").toInt,props("resolver.path"))
		}
	}
	val publishTo = sbtPublishResolver
	// *-- End Copy of ResolverPlugin contents
	// Publish settings
	override def managedStyle = ManagedStyle.Maven
}
