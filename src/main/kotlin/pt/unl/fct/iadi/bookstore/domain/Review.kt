package pt.unl.fct.iadi.bookstore.domain

import java.util.UUID

data class Review(
    val id: UUID = UUID.randomUUID(),
    val rating: Int,
    val comment: String?,
) {
    init {
        require(rating in 1..5) {
            "Rating must be between 1 and 5"
        }
        if(comment != null){
            require(comment.length in 0..500){
                "Comment must be between 0 and 500"
            }
        }
    }
}