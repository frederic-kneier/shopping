package de.solugo.shopping

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app")
data class ApplicationProperties(
    val security: Security = Security(),
    val mongo: Mongo = Mongo(),
    val rest: Rest = Rest(),
) {

    data class Security(
        val issuerUri: String = "http://localhost:9100/auth",
        val publicClientId: String = "public",
    )

    data class Mongo(
        val connectionUri: String = "mongodb://localhost:27017",
        val database: String = "shopping",
    )

    data class Rest(
        val defaultPageSize: Int = 10,
        val maxPageSize: Int = 1000,
    )
}
