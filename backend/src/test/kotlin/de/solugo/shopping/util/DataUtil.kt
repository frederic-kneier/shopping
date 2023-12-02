package de.solugo.shopping.util

import de.solugo.shopping.model.ShoppingEntry
import de.solugo.shopping.model.ShoppingList
import org.litote.kmongo.coroutine.CoroutineCollection

object DataUtil {

    suspend fun CoroutineCollection<ShoppingList>.createShoppingList(
        principalId: String = uuid(),
        role: ShoppingList.Role = ShoppingList.Role.ADMIN,
        id: String = uuid(),
        title: String = "List $id",
    ) = ShoppingList(
        id = id,
        title = title,
        principalRoles = mapOf(
            principalId to role,
        )
    ).also {
        save(it)
    }

    suspend fun CoroutineCollection<ShoppingEntry>.createShoppingEntry(
        listId: String = uuid(),
        id: String = uuid(),
        title: String = "List $id",
        amount: Int = 1,
        state: ShoppingEntry.State = ShoppingEntry.State.PENDING,
    ) = ShoppingEntry(
        listId = listId,
        id = id,
        title = title,
        amount = amount,
        state = state,
    ).also {
        save(it)
    }


}