package pt.unl.fct.iadi.bookstore.controller.dto

data class BookDTO(
    val isbn: String,
    val title: String,
    val author: String,
    val price: Double,
    val image: String
) {
}