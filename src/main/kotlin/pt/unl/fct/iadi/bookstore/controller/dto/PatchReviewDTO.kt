package pt.unl.fct.iadi.bookstore.controller.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

class PatchReviewDTO(
    @field:Min(value = 1, message = "Rating must be between 1 and 5")
    @field:Max(value = 5, message = "Rating must be between 1 and 5")
    val rating: Int? = null,

    @field:Size(min = 0, max = 500, message = "Comment must be between 0 and 500")
    val comment: String? = null
) {
}