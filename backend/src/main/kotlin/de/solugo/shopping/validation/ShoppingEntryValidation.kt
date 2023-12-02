package de.solugo.shopping.validation

import de.solugo.shopping.model.ShoppingEntry
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.validate

object ShoppingEntryValidation {
    fun ShoppingEntry.validateSave() = apply {
        validate(this) {
            validate(ShoppingEntry::listId).isNotNull()
            validate(ShoppingEntry::id).isNotNull()
            validate(ShoppingEntry::title).isNotNull().isNotBlank()
        }
    }
}