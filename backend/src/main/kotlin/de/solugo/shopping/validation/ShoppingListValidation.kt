package de.solugo.shopping.validation

import de.solugo.shopping.model.ShoppingList
import org.valiktor.functions.containsAnyValue
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate

object ShoppingListValidation {
    fun ShoppingList.validateSave() = apply  {
        validate(this) {
            validate(ShoppingList::id).isNotNull()
            validate(ShoppingList::title).isNotNull().isNotBlank()
            validate(ShoppingList::principalRoles).containsAnyValue(ShoppingList.Role.ADMIN)
        }
    }
}