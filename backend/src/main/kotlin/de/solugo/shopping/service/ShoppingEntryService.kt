package de.solugo.shopping.service

import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import de.solugo.shopping.ApplicationProperties
import de.solugo.shopping.model.ShoppingEntry
import de.solugo.shopping.util.pageSize
import de.solugo.shopping.validation.ShoppingEntryValidation.validateSave
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineCollection
import org.springframework.stereotype.Service

@Service
class ShoppingEntryService(
    private val shoppingEntryCollection: CoroutineCollection<ShoppingEntry>,
    private val properties: ApplicationProperties,
) {

    suspend fun findAll(
        listId: String,
        pageSize: Int? = null,
        term: String? = null,
    ) = run {
        shoppingEntryCollection.find(
            filter = combine(
                buildList {
                    add(ShoppingEntry::listId.eq(listId))
                    if (term != null) add(ShoppingEntry::title.regex("^${Regex.escape(term)}"))
                }
            )
        ).limit(
            limit = properties.rest.pageSize(pageSize),
        ).toList()
    }

    suspend fun createOne(shoppingEntry: ShoppingEntry) = run {
        val actual = shoppingEntry.validateSave()
        val result = shoppingEntryCollection.findOneAndUpdate(
            options = FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.BEFORE),
            filter = combine(
                ShoppingEntry::listId eq shoppingEntry.listId,
                ShoppingEntry::id eq shoppingEntry.id,
            ),
            update = combine(
                setValueOnInsert(actual),
            ),
        )
        actual.takeIf { result == null }
    }

    suspend fun updateOne(entry: ShoppingEntry) = run {
        shoppingEntryCollection.findOneAndUpdate(
            options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER),
            filter = combine(
                ShoppingEntry::listId.eq(entry.listId),
                ShoppingEntry::id.eq(entry.id),
            ),
            update = set(
                ShoppingEntry::title.setTo(entry.title),
                ShoppingEntry::amount.setTo(entry.amount),
                ShoppingEntry::state.setTo(entry.state),
            ),
        )
    }

    suspend fun deleteOne(listId: String, entryId: String) = shoppingEntryCollection.deleteOne(
        filter = combine(
            ShoppingEntry::listId.eq(listId),
            ShoppingEntry::id.eq(entryId),
        ),
    ).deletedCount == 1L


}