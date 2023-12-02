package de.solugo.shopping.util

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException


fun httpNotFound(): Nothing = throw ResponseStatusException(HttpStatus.NOT_FOUND)
fun httpForbidden(): Nothing = throw ResponseStatusException(HttpStatus.FORBIDDEN)