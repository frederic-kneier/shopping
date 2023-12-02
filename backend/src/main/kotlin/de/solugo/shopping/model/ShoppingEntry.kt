package de.solugo.shopping.model

import org.bson.codecs.pojo.annotations.BsonId

data class ShoppingEntry(
    @BsonId val id: String? = null,
    val listId: String? = null,
    val title: String? = null,
    val amount: Int? = null,
    val state: State? = null,
) {
    enum class State {
        PENDING, DONE
    }
}
