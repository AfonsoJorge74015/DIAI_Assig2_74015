package pt.unl.fct.iadi.bookstore.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.bookstore.controller.dto.CreateBookRequest
import pt.unl.fct.iadi.bookstore.controller.dto.ReviewDTO
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.domain.Review
import pt.unl.fct.iadi.bookstore.service.exceptions.BookstoreExceptions
import pt.unl.fct.iadi.bookstore.utils.Mappers
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.indexOfFirst

@Service
class BookstoreService(
    private val mappers: Mappers,
) {
    //books = isbn -> book
    private val books = ConcurrentHashMap<String, Book>()

    //reviews = book isbn -> list of reviews
    private val reviews = ConcurrentHashMap<String, MutableList<Review>>()

    //1
    fun getBooks() : List<CreateBookRequest> {
        return books.values.toList()
            .map { mappers.bookToDto(it) }
    }

    //2
    fun createBook(createBookRequest: CreateBookRequest) {
        if(books.containsKey(createBookRequest.isbn)){
            throw BookstoreExceptions.AlreadyExistsException(createBookRequest.isbn)
        }
        books[createBookRequest.isbn] = mappers.dtoToBook(createBookRequest)
        reviews[createBookRequest.isbn] = mutableListOf()
    }

    //3
    fun getBook(isbn: String): CreateBookRequest {
        val bookDto = books[isbn] ?: throw BookstoreExceptions.NotFoundException(isbn)
        return mappers.bookToDto(bookDto)
    }

    //4
    fun updateBook(isbn: String, createBookRequest: CreateBookRequest) {
        if(books.containsKey(isbn)){
            books[isbn] = mappers.dtoToBook(createBookRequest)
        } else {
            createBook(createBookRequest)
        }
    }

    //5
    fun patchBook(isbn: String, fields: Map<String, Any>) {
        val toPatch = books[isbn] ?: throw BookstoreExceptions.NotFoundException(isbn)

        val patched = toPatch.copy(
            title = fields["title"] as? String ?: toPatch.title,
            author = fields["author"] as? String ?: toPatch.author,
            price = fields["price"]?.toString()?.toDoubleOrNull() ?: toPatch.price,
            image = fields["image"] as? String ?: toPatch.image,
        )

        val validated = Book(
            isbn,
            patched.title,
            patched.author,
            patched.price,
            patched.image
        )

        books[isbn] = validated
    }

    //6
    fun deleteBook(isbn: String) {
        books[isbn] ?: throw BookstoreExceptions.NotFoundException(isbn)
        books.remove(isbn)
        reviews.remove(isbn)
    }

    //7
    fun getReviews(isbn: String): List<ReviewDTO> {
        val reviews = reviews[isbn] ?: throw BookstoreExceptions.NotFoundException(isbn)
        return reviews.map { mappers.reviewToDto(it) }
    }

    //8
    fun addReview(isbn: String, reviewDto: ReviewDTO)
        : ReviewDTO {
        val book = reviews[isbn] ?: throw BookstoreExceptions.NotFoundException(isbn)
        val review = mappers.dtoToReview(reviewDto)
        book.add(review)
        reviews[isbn] = book
        return mappers.reviewToDto(review)
    }

    //9
    fun updateReview(isbn: String, reviewId: String, reviewDto: ReviewDTO) {

        val reviews = reviews[isbn] ?: throw BookstoreExceptions.NotFoundException(isbn)
        val targetReview = UUID.fromString(reviewId)
        val index = reviews.indexOfFirst{ it.id == targetReview }

        if(index == -1)
            throw BookstoreExceptions.NotFoundException(reviewId)

        reviews[index] = mappers.dtoToReview(reviewDto)
    }

    //10
    fun patchReview(isbn: String, reviewId: String, fields: Map<String, Any>) {

        val reviews = reviews[isbn] ?: throw BookstoreExceptions.NotFoundException(isbn)
        val targetReview = UUID.fromString(reviewId)
        val index = reviews.indexOfFirst { it.id == targetReview }

        if(index == -1)
            throw BookstoreExceptions.NotFoundException(reviewId)

        val toPatch = reviews[index]

        val patched = toPatch.copy(
            author = fields["author"] as? String ?: toPatch.author,
            rating = fields["rating"]?.toString()?.toIntOrNull() ?: toPatch.rating,
            comment = fields["comment"] as? String ?: toPatch.comment)

        val validated = Review(
            author = patched.author,
            rating = patched.rating,
            comment = patched.comment)

        reviews[index] = validated
    }

    //11
    fun deleteReview(isbn: String, reviewId: String) {
        val bookReviews = reviews[isbn] ?: throw BookstoreExceptions.NotFoundException(isbn)
        val targetReview = UUID.fromString(reviewId)
        val index = bookReviews.indexOfFirst { it.id == targetReview }

        if(index != -1) {
            bookReviews.removeAt(index)
            reviews[isbn] = bookReviews
        } else {
            throw BookstoreExceptions.NotFoundException(reviewId)
        }
    }

    //security helpers
    fun checkReviewAuthor(isbn: String, reviewId: String, username: String): Boolean {
        val bookReviews = reviews[isbn] ?: return false
        val review = bookReviews.find { it.id == UUID.fromString(reviewId) } ?: return false
        return review.author == username
    }
}