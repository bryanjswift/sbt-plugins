package bjs.project

import sbt._

trait ResolverPlugin extends BasicManagedProject {
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
}
