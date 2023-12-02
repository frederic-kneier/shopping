package de.solugo.shopping.controller

import de.solugo.shopping.model.ShoppingList
import de.solugo.shopping.model.ShoppingList.Role.ADMIN
import de.solugo.shopping.security.PrincipalAccess.checkRole
import de.solugo.shopping.security.PrincipalAuthentication
import de.solugo.shopping.service.ShoppingListService
import de.solugo.shopping.util.httpForbidden
import de.solugo.shopping.util.httpNotFound
import de.solugo.shopping.util.uuid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/lists")
class ShoppingListController(
    private val service: ShoppingListService,
) {

    @GetMapping
    suspend fun findAll(
        @AuthenticationPrincipal authentication: PrincipalAuthentication,
        @RequestParam("id") ids: Collection<String> = emptyList(),
        @RequestParam pageSize: Int? = null,
    ) = run {
        service.findAll(
            pageSize = pageSize,
            ids = authentication.shoppingListRoles.keys.run {
                ids.takeIf { it.isNotEmpty() }?.filter { contains(it) } ?: this
            },
        )
    }

    @PostMapping
    suspend fun createOne(
        @AuthenticationPrincipal authentication: PrincipalAuthentication,
        @RequestBody shoppingList: ShoppingList,
    ) = run {
        service.createOne(
            shoppingList = shoppingList.copy(
                id = shoppingList.id ?: uuid(),
                principalRoles = shoppingList.principalRoles + (authentication.principal to ADMIN)
            ),
        ) ?: httpForbidden()
    }

    @PostMapping("/{listId}")
    suspend fun updateOne(
        @AuthenticationPrincipal authentication: PrincipalAuthentication,
        @PathVariable listId: String,
        @RequestBody shoppingList: ShoppingList,
    ) = run {
        authentication.checkRole(listId, ADMIN)
        service.updateOne(
            shoppingList = shoppingList.copy(
                id = listId
            ),
        ) ?: httpNotFound()
    }

    @DeleteMapping("/{listId}")
    suspend fun deleteOne(
        @AuthenticationPrincipal authentication: PrincipalAuthentication,
        @PathVariable listId: String,
    ) = run {
        authentication.checkRole(listId, ADMIN)
        service.deleteOne(listId) || httpNotFound()
    }

}