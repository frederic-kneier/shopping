package de.solugo.shopping.security

import de.solugo.shopping.model.ShoppingList
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt

class PrincipalAuthentication(
    private val principalId: String,
    private val jwt: Jwt,
    val shoppingListRoles: Map<String, ShoppingList.Role>,
) : AbstractAuthenticationToken(emptyList()) {
    override fun getCredentials() = jwt
    override fun getPrincipal() = principalId
    override fun isAuthenticated() = true
}