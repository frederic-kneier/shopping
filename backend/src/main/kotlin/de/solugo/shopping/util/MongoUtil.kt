package de.solugo.shopping.util

import org.bson.conversions.Bson
import org.litote.kmongo.combine

fun combineNotNull(vararg items: Bson?) = combine(listOfNotNull(*items))