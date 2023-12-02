package de.solugo.shopping.controller

import de.solugo.shopping.IntegrationTest
import de.solugo.shopping.model.ShoppingEntry
import de.solugo.shopping.model.ShoppingList
import de.solugo.shopping.util.DataUtil.createShoppingEntry
import de.solugo.shopping.util.DataUtil.createShoppingList
import de.solugo.shopping.util.uuid
import io.kotest.assertions.json.FieldComparison
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.litote.kmongo.coroutine.CoroutineCollection
import org.springframework.beans.factory.annotation.Autowired

class ShoppingEntryControllerTest : IntegrationTest() {

    @Autowired
    private lateinit var shoppingListCollection: CoroutineCollection<ShoppingList>

    @Autowired
    private lateinit var shoppingEntryCollection: CoroutineCollection<ShoppingEntry>

    @Test
    fun `create new entry`() = runTest {
        val principalId = uuid()
        val list = shoppingListCollection.createShoppingList(principalId = principalId)

        rest.post("/api/lists/${list.id}/entries") {
            bearerToken(principalId)
            setBody(
                mapOf(
                    "title" to "Test",
                    "amount" to 1
                )
            )
        }.apply {
            status shouldBe HttpStatusCode.OK
        }
    }

    @Test
    fun `get entries`() = runTest {
        val principalId = uuid()
        val list = shoppingListCollection.createShoppingList(principalId = principalId)
        val entry = shoppingEntryCollection.createShoppingEntry(listId = list.id!!)

        rest.get("/api/lists/${list.id}/entries") {
            bearerToken(principalId)
        }.apply {
            status shouldBe HttpStatusCode.OK
            bodyAsText() shouldEqualJson {
                """
                [
                    {
                        "listId": "${entry.listId}",
                        "id" : "${entry.id}",
                        "title": "${entry.title}",
                        "amount": ${entry.amount},
                        "state": "${entry.state}"
                    }
                ] 
                """
            }
        }
    }

    @Test
    fun `update entry`() = runTest {
        val principalId = uuid()
        val list = shoppingListCollection.createShoppingList(principalId = principalId)
        val entry = shoppingEntryCollection.createShoppingEntry(
            listId = list.id!!,
            title = "Old Title",
            amount = 1,
            state = ShoppingEntry.State.PENDING,
        )

        rest.post("/api/lists/${list.id}/entries/${entry.id}") {
            bearerToken(principalId)
            setBody(
                mapOf(
                    "title" to "New Title",
                    "amount" to 2,
                    "state" to "DONE",
                )
            )
        }.apply {
            status shouldBe HttpStatusCode.OK
            bodyAsText() shouldEqualJson {
                fieldComparison = FieldComparison.Lenient
                """
                {
                    "id" : "${entry.id}",
                    "listId" : "${list.id}",
                    "title": "New Title",
                    "amount": 2,
                    "state": "DONE"
                }
                """
            }

            shoppingEntryCollection.findOneById(entry.id!!).also {
                it?.title shouldBe "New Title"
                it?.amount shouldBe 2
                it?.state shouldBe ShoppingEntry.State.DONE
            }
        }
    }

    @Test
    fun `delete list`() = runTest {
        val principalId = uuid()
        val list = shoppingListCollection.createShoppingList(principalId = principalId)
        val entry = shoppingEntryCollection.createShoppingEntry(listId = list.id!!)

        rest.delete("/api/lists/${list.id}/entries/${entry.id}") {
            bearerToken(principalId)
        }.apply {
            status shouldBe HttpStatusCode.OK
            shoppingEntryCollection.findOneById(entry.id!!) shouldBe null
        }
    }

}
