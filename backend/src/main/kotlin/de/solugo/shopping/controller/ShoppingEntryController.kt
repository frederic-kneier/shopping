package de.solugo.shopping.controller

import de.solugo.shopping.model.ShoppingEntry
import de.solugo.shopping.model.ShoppingList.Role.*
import de.solugo.shopping.security.PrincipalAccess.checkRole
import de.solugo.shopping.security.PrincipalAuthentication
import de.solugo.shopping.service.ShoppingEntryService
import de.solugo.shopping.util.httpForbidden
import de.solugo.shopping.util.httpNotFound
import de.solugo.shopping.util.uuid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/lists/{listId}/entries")
class ShoppingEntryController(
    private val service: ShoppingEntryService,
) {

    @GetMapping
    suspend fun findAll(
        @AuthenticationPrincipal authentication: PrincipalAuthentication,
        @PathVariable listId: String,
        @RequestParam term: String? = null,
        @RequestParam pageSize: Int? = null,
    ) = run {
        authentication.checkRole(listId, ADMIN, EDITOR, VIEWER)

        service.findAll(
            listId = listId,
            term = term,
            pageSize = pageSize,
        )
    }

    @PostMapping
    suspend fun createOne(
        @AuthenticationPrincipal authentication: PrincipalAuthentication,
        @PathVariable listId: String,
        @RequestBody entry: ShoppingEntry,
    ) = run {
        authentication.checkRole(listId, ADMIN, EDITOR)

        service.createOne(
            entry.copy(
                listId = listId,
                id = entry.id ?: uuid(),
            )
        ) ?: httpForbidden()
    }

    @PostMapping("/{entryId}")
    suspend fun updateOne(
        @AuthenticationPrincipal authentication: PrincipalAuthentication,
        @PathVariable listId: String,
        @PathVariable entryId: String,
        @RequestBody entry: ShoppingEntry,
    ) = run {
        authentication.checkRole(listId, ADMIN, EDITOR)

        service.updateOne(
            entry.copy(
                listId = listId,
                id = entryId,
            )
        ) ?: httpNotFound()
    }

    @DeleteMapping("/{entryId}")
    suspend fun deleteOne(
        @AuthenticationPrincipal authentication: PrincipalAuthentication,
        @PathVariable listId: String,
        @PathVariable entryId: String,
    ) = run {
        authentication.checkRole(listId, ADMIN, EDITOR)

        service.deleteOne(
            listId = listId,
            entryId = entryId,
        ) || httpNotFound()
    }

}