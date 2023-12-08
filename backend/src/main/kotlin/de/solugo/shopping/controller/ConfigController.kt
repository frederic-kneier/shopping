package de.solugo.shopping.controller

import de.solugo.shopping.ApplicationProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ConfigController(
    private val properties: ApplicationProperties,
) {

    @GetMapping("/config")
    fun get() = Config(
        issuerUri = properties.security.issuerUri,
        publicClientId = properties.security.publicClientId,
    )

    data class Config(
        val issuerUri: String,
        val publicClientId: String,
    )
}