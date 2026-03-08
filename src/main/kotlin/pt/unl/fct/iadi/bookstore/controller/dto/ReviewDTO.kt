package pt.unl.fct.iadi.bookstore.controller.dto

import java.util.UUID

data class ReviewDTO(
    val id: UUID?,
    val rating: Int,
    val comment: String?
) {
}