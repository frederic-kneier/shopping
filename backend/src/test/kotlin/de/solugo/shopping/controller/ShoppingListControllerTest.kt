package de.solugo.shopping.controller

import de.solugo.shopping.IntegrationTest
import de.solugo.shopping.model.ShoppingList
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

class ShoppingListControllerTest : IntegrationTest() {

    @Autowired
    private lateinit var shoppingListCollection : CoroutineCollection<ShoppingList>

    @Test
    fun `create new list`() = runTest {
        rest.post("/api/lists")  {
            bearerToken(uuid())
            setBody(
                mapOf(
                    "title" to "Test",
                )
            )
        }.apply {
            status shouldBe HttpStatusCode.OK
        }
    }

    @Test
    fun `get own lists`() = runTest {
        val principalId = uuid()
        val ownList = shoppingListCollection.createShoppingList(
            principalId = principalId
        )

        shoppingListCollection.createShoppingList()

        rest.get("/api/lists")  {
            bearerToken(principalId)
        }.apply {
            status shouldBe HttpStatusCode.OK
            bodyAsText() shouldEqualJson  {
                """
                [
                    {
                        "id" : "${ownList.id}",
                        "title": "${ownList.title}",
                        "principalRoles": {
                            "$principalId": "ADMIN"
                        }
                    }
                ] 
                """
            }
        }
    }

    @Test
    fun `update list`() = runTest {
        val principalId = uuid()
        val listId = uuid()
        val list = shoppingListCollection.createShoppingList(
            principalId = principalId,
            id = listId,
            title = "Old Title",
        )

        rest.post("/api/lists/${list.id}")  {
            bearerToken(principalId)
            setBody(
                mapOf(
                    "title" to "New Title",
                )
            )
        }.apply {
            status shouldBe HttpStatusCode.OK
            bodyAsText() shouldEqualJson  {
                fieldComparison = FieldComparison.Lenient
                """
                {
                    "id" : "$listId",
                    "title": "New Title"
                }
                """
            }

            shoppingListCollection.findOneById(listId)?.title shouldBe "New Title"
        }
    }

    @Test
    fun `delete list`() = runTest {
        val principalId = uuid()
        val listId = uuid()
        val list = shoppingListCollection.createShoppingList(
            principalId = principalId,
            id = listId
        )

        rest.delete("/api/lists/${list.id}")  {
            bearerToken(principalId)
            setBody(
                mapOf(
                    "title" to "New Title",
                )
            )
        }.apply {
            status shouldBe HttpStatusCode.OK
            shoppingListCollection.findOneById(listId) shouldBe null
        }
    }

}
