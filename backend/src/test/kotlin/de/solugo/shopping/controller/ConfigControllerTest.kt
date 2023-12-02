package de.solugo.shopping.controller

import de.solugo.shopping.IntegrationTest
import io.kotest.assertions.json.FieldComparison
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ConfigControllerTest : IntegrationTest() {

    @Test
    fun `retrieve configuration`() = runTest {
        rest.get("/api/config").apply {
            status shouldBe HttpStatusCode.OK
            bodyAsText() shouldEqualJson {
                fieldComparison = FieldComparison.Lenient
                """
                    {}    
                """
            }
        }
    }

}