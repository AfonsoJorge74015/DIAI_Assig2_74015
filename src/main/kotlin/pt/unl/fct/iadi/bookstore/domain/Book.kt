package pt.unl.fct.iadi.bookstore.domain

import java.net.URL

data class Book(
    val isbn: String,
    val title: String,
    val author: String,
    val price: Double,
    val image: String
) {
    init {
        require(isbn.isNotBlank()) { "ISBN cannot be blank" }

        require(title.length in 1..120) {
            "Title must be between 1 and 120 characters"
        }

        require(author.length in 1..80) {
            "Author must be between 1 and 80 characters"
        }

        require(price > 0) {
            "Price must be greater than zero"
        }

        require(isValidUrl(image)) {
            "Image must have a valid URL"
        }
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            val parsedUrl = URL(url)
            parsedUrl.protocol == "http" || parsedUrl.protocol == "https"
        } catch (e: Exception) {
            false
        }
    }
}
