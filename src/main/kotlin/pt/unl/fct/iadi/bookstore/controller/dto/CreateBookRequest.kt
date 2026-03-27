package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL

data class CreateBookRequest(
    @field:NotBlank(message = "ISBN cannot be blank")
    @field:Schema(description = "The unique ISBN-13 of the book", example = "9780134685991")
    val isbn: String,

    @field:NotBlank
    @field:Size(min = 1, max = 120, message = "The length of the book must be between 1 and 120 characters")
    @field:Schema(description = "The title of the book", minLength = 1, maxLength = 120, example = "Effective Java")
    val title: String,

    @field:NotBlank
    @field:Size(min = 1, max = 80, message = "The name of the author must be between 1 and 80 characters")
    @field:Schema(description = "The author of the book", minLength = 1, maxLength = 80, example = "Joshua Bloch")
    val author: String,

    @field:Positive
    @field:Schema(description = "The price of the book in EUR", minimum = "0.01", example = "45.0")
    val price: Double,

    @field:NotBlank
    @field:URL
    @field:Schema(description = "A valid URL for the book cover image", example = "https://example.com/cover.jpg")
    val image: String
) {
}