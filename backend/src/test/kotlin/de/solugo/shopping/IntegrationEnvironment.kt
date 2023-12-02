package de.solugo.shopping

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName.parse

object IntegrationEnvironment {

    fun start() = runBlocking(Dispatchers.IO) {
        listOf(Mongo, OAuth).forEach {
            launch { it.start() }
        }
    }

    object Mongo : MongoDBContainer(parse("mongo:4.4.26"))
    object OAuth : GenericContainer<OAuth>(parse("ghcr.io/navikt/mock-oauth2-server:2.0.0")) {
        val issuerUri by lazy { "http://$host:$firstMappedPort/auth" }

        override fun configure() {
            withExposedPorts(8080)
        }
    }

}