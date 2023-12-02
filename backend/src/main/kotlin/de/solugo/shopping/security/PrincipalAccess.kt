package de.solugo.shopping.security

import de.solugo.shopping.model.ShoppingList
import de.solugo.shopping.util.httpForbidden

object PrincipalAccess {
    fun PrincipalAuthentication.checkRole(shoppingListId: String, vararg roles: ShoppingList.Role) {
        when {
            roles.isEmpty() -> shoppingListRoles.containsKey(shoppingListId) || httpForbidden()
            else -> roles.contains(shoppingListRoles[shoppingListId]) || httpForbidden()
        }
    }
}