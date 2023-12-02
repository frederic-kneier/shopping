package de.solugo.shopping.util

import de.solugo.shopping.ApplicationProperties
import kotlin.math.min

fun ApplicationProperties.Rest.pageSize(value: Int?) = min(maxPageSize, value ?: defaultPageSize)