package pt.unl.fct.iadi.bookstore.controller

import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.domain.Review
import pt.unl.fct.iadi.bookstore.service.BookstoreService

class BookstoreController(
    private val service: BookstoreService
) : BookstoreAPI {

    override fun getBooks(): List<Book> = service.getBooks()

    override fun addBook(book: Book): Book {
        TODO("Not yet implemented")
    }

    override fun getBook(id: String): Book {
        TODO("Not yet implemented")
    }

    override fun upsertBook(
        id: String,
        book: Book
    ): Book {
        TODO("Not yet implemented")
    }

    override fun patchBook(
        id: String,
        fields: Map<String, Any>
    ): Book {
        TODO("Not yet implemented")
    }

    override fun deleteBook(id: String) {
        TODO("Not yet implemented")
    }

    override fun getReviews(id: String): List<Review> {
        TODO("Not yet implemented")
    }

    override fun addReview(
        id: String,
        review: Review
    ): Book {
        TODO("Not yet implemented")
    }

    override fun upsertReview(
        id: String,
        reviewId: String,
        review: Review
    ): Book {
        TODO("Not yet implemented")
    }

    override fun patchReview(
        id: String,
        reviewId: String,
        fields: Map<String, Any>
    ): Book {
        TODO("Not yet implemented")
    }

    override fun deleteReview(id: String, reviewId: String) {
        TODO("Not yet implemented")
    }


}