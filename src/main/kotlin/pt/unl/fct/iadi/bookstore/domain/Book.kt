package pt.unl.fct.iadi.bookstore.domain

data class Book(
    val isbn: String,
    val title: String,
    val author: String,
    val price: Double,
    val image: String
)
