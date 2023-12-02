package de.solugo.shopping.config

import de.solugo.shopping.ApplicationProperties
import de.solugo.shopping.security.PrincipalAuthentication
import de.solugo.shopping.service.ShoppingListService
import kotlinx.coroutines.reactor.mono
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders.fromOidcIssuerLocation
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class SecurityConfiguration {

    @Bean
    fun securityChain(
        http: ServerHttpSecurity,
        properties: ApplicationProperties,
        shoppingListService: ShoppingListService,
    ): SecurityWebFilterChain = http.run {
        csrf { csrf ->
            csrf.disable()
        }
        oauth2ResourceServer { server ->
            server.jwt { jwt ->
                jwt.jwtDecoder(fromOidcIssuerLocation(properties.security.issuerUri))
                jwt.jwtAuthenticationConverter {
                    mono {
                        PrincipalAuthentication(
                            principalId = it.subject,
                            jwt = it,
                            shoppingListRoles = shoppingListService.getPrincipalRoles(it.subject),
                        )
                    }
                }
            }
        }
        authorizeExchange {
            it.pathMatchers("/api/config").permitAll()
            it.pathMatchers("/api/**").authenticated()
            it.anyExchange().permitAll()
        }
        build()
    }
}