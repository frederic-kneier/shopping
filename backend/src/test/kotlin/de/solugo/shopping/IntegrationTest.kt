package de.solugo.shopping

import com.fasterxml.jackson.databind.node.ObjectNode
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [Application::class, IntegrationEnvironment::class],
)
abstract class IntegrationTest {

    @Autowired
    protected lateinit var rest: HttpClient


    protected suspend fun HttpRequestBuilder.bearerToken(principalId: String) = run {
        rest.post("${IntegrationEnvironment.OAuth.issuerUri}/token") {
            val parameters = parameters {
                append("grant_type", "password")
                append("client_id", "test")
                append("username", principalId)
                append("password", "password")
            }
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(FormDataContent(parameters))
        }.apply {
            bearerAuth(body<ObjectNode>().at("/access_token").textValue())
        }
    }

    companion object {
        @JvmStatic
        @DynamicPropertySource
        @Suppress("unused")
        fun configure(registry: DynamicPropertyRegistry) {
            IntegrationEnvironment.start()
            registry.add("app.mongo.connectionUri") { IntegrationEnvironment.Mongo.connectionString }
            registry.add("app.security.issuerUri") { IntegrationEnvironment.OAuth.issuerUri }
        }
    }


}