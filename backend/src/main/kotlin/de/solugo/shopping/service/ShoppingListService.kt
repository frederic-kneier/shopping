package de.solugo.shopping.service

import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import de.solugo.shopping.ApplicationProperties
import de.solugo.shopping.model.ShoppingList
import de.solugo.shopping.util.combineNotNull
import de.solugo.shopping.util.pageSize
import de.solugo.shopping.validation.ShoppingListValidation.validateSave
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.aggregate
import org.springframework.stereotype.Service

@Service
class ShoppingListService(
    private val shoppingListCollection: CoroutineCollection<ShoppingList>,
    private val properties: ApplicationProperties,
) {

    suspend fun findAll(
        ids: Collection<String>,
        pageSize: Int? = null,
    ) = run {
        shoppingListCollection.find(
            filter = combineNotNull(
                ids.takeIf { it.isNotEmpty() }?.let { ShoppingList::id.`in`(it) },
            )
        ).limit(
            limit = properties.rest.pageSize(pageSize),
        ).toList()
    }

    suspend fun createOne(shoppingList: ShoppingList) = run {
        val actual = shoppingList.validateSave()
        val result = shoppingListCollection.findOneAndUpdate(
            options = FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.BEFORE),
            filter = combine(
                ShoppingList::id eq shoppingList.id,
            ),
            update = combine(
                setValueOnInsert(actual)
            ),
        )
        actual.takeIf { result == null }
    }

    suspend fun updateOne(shoppingList: ShoppingList) = run {
        shoppingListCollection.findOneAndUpdate(
            options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER),
            filter = combine(
                ShoppingList::id eq shoppingList.id,
            ),
            update = set(
                ShoppingList::title.setTo(shoppingList.title),
            ),
        )
    }

    suspend fun deleteOne(id: String) = run {
        shoppingListCollection.deleteOne(
            filter = combine(
                ShoppingList::id eq id,
            ),
        ).deletedCount == 1L
    }

    suspend fun getPrincipalRoles(principalId: String) = run {
        shoppingListCollection.aggregate<ShoppingListRightsEntry>(
            match(
                ShoppingList::principalRoles.keyProjection(principalId).exists()
            ),
            project(
                ShoppingListRightsEntry::id from ShoppingList::id,
                ShoppingListRightsEntry::role from ShoppingList::principalRoles.keyProjection(principalId),
            ),
        ).toList().associate {
            it.id to it.role
        }
    }

    private data class ShoppingListRightsEntry(
        val id: String,
        val role: ShoppingList.Role,
    )
}