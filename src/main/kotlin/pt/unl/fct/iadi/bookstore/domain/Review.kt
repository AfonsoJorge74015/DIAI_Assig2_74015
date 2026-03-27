package pt.unl.fct.iadi.bookstore.domain

import java.util.UUID

data class Review(
    val id: UUID = UUID.randomUUID(),
    val author: String,
    val rating: Int,
    val comment: String?
)