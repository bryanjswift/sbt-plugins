package bjs.project

import java.io.IOException
import sbt._

trait ResolverPlugin extends BasicManagedProject {
	val resolverPath = propertyOptional[String](".resolver")
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
			case ioe:IOException => log.error(String.format("Resolver configuration could not be found at '%s'. You will not be able to publish without this file.",resolverPath.value))
			case iae:IllegalArgumentException => log.error(String.format("Resolver configuration could not be found at '%s'. You will not be able to publish without this file.",resolverPath.value))
		}
		props("resolver.type") match {
			case "sftp" =>
				Resolver.sftp(
					props("resolver.name"),
					props("resolver.host"),
					props("resolver.port").toInt,
					props("resolver.path"))
			case "ssh" =>
				Resolver.ssh(
					props("resolver.name"),
					props("resolver.host"),
					props("resolver.port").toInt,
					props("resolver.path"))
			case s =>
				log.warn(String.format("Unknown resolver type %s"))
		}
	}
	val publishTo = sbtPublishResolver
}
