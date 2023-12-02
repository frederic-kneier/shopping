package de.solugo.shopping

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get

@Configuration
class IntegrationConfiguration {

    @Bean
    fun ktorTestClient(
        environment: Environment,
    ) = HttpClient(Java) {
        install(ContentNegotiation) {
            jackson()
        }
        install(DefaultRequest) {
            url("http://localhost:${environment["local.server.port"]}")
            contentType(ContentType.Application.Json)
        }
    }

}