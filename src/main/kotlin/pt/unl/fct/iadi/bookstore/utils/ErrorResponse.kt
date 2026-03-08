package pt.unl.fct.iadi.bookstore.utils

import io.swagger.v3.oas.annotations.media.Schema

class ErrorResponse(
    @Schema(description = "Machine-readable error identifier", example = "NOT_FOUND")
    val error: String,
    @Schema(description = "Human-readable description of the error", example = "Book with ISBN 9780134685991 not found")
    val message: String
) {
}