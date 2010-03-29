package bjs.project

import java.io.IOException
import sbt._

trait ResolverPlugin extends BasicManagedProject {
	val resolverPath = propertyOptional[String](".resolver")
	val defaultResolverName = "Example Resolver"
	val defaultResolverHost = "example.com"
	val defaultResolverPort = "22"
	val defaultResolverPath = "/path/to/publish/into/"
	val defaultResolverType = "sftp"
	private def sbtPublishResolver = {
		import java.io.FileInputStream
		import java.util.Properties
		val props = new Properties() {
			def apply(key:String) = this.getProperty(key)
			def apply(key:String,default:String) = this.getProperty(key,default)
		}
		try {
			props.load(new FileInputStream(resolverPath.value))
		} catch {
			case ioe:IOException => log.warn(String.format("Resolver configuration could not be found at '%s'",resolverPath.value))
			case iae:IllegalArgumentException => log.warn(String.format("Resolver configuration could not be found at '%s'",resolverPath.value))
		}
		props("resolver.type",defaultResolverType) match {
			case "sftp" =>
				Resolver.sftp(
					props("resolver.name",defaultResolverName),
					props("resolver.host",defaultResolverHost),
					props("resolver.port",defaultResolverPort).toInt,
					props("resolver.path",defaultResolverPath))
			case "ssh" =>
				Resolver.ssh(
					props("resolver.name",defaultResolverName),
					props("resolver.host",defaultResolverHost),
					props("resolver.port",defaultResolverPort).toInt,
					props("resolver.path",defaultResolverPath))
		}
	}
	val publishTo = sbtPublishResolver
}
