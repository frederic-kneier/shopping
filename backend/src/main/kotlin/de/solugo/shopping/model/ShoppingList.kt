package de.solugo.shopping.model

import org.bson.codecs.pojo.annotations.BsonId

data class ShoppingList(
    @BsonId val id: String? = null,
    val title: String? = null,
    val principalRoles: Map<String, Role> = emptyMap(),
) {

    enum class Role {
        ADMIN, EDITOR, VIEWER
    }

}
