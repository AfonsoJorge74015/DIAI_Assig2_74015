package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

data class BookDTO(
    @Schema(description = "The unique ISBN-13 of the book", example = "9780134685991")
    val isbn: String,
    @Schema(description = "The title of the book", minLength = 1, maxLength = 120, example = "Effective Java")
    val title: String,
    @Schema(description = "The author of the book", minLength = 1, maxLength = 80, example = "Joshua Bloch")
    val author: String,
    @Schema(description = "The price of the book in EUR", minimum = "0.01", example = "45.0")
    val price: Double,
    @Schema(description = "A valid URL for the book cover image", example = "https://example.com/cover.jpg")
    val image: String
) {
}