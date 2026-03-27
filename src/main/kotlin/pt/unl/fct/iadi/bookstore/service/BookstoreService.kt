package pt.unl.fct.iadi.bookstore.service

import jakarta.validation.ConstraintViolationException
import org.springframework.stereotype.Service
import jakarta.validation.Validator
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import pt.unl.fct.iadi.bookstore.controller.dto.BookDTO
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
    private val validator: Validator
) {
    //books = isbn -> book
    private val books = ConcurrentHashMap<String, Book>()

    //reviews = book isbn -> list of reviews
    private val reviews = ConcurrentHashMap<String, MutableList<Review>>()

    //1
    fun getBooks() : List<BookDTO> {
        return books.values.toList()
            .map { mappers.bookToDto(it) }
    }

    //2
    fun createBook(bookDTO: BookDTO) {
        if(books.containsKey(bookDTO.isbn)){
            throw BookstoreExceptions.AlreadyExistsException(bookDTO.isbn)
        }
        books[bookDTO.isbn] = mappers.dtoToBook(bookDTO)
        reviews[bookDTO.isbn] = mutableListOf()
    }

    //3
    fun getBook(isbn: String): BookDTO {
        val bookDto = books[isbn] ?: throw BookstoreExceptions.NotFoundException(isbn)
        return mappers.bookToDto(bookDto)
    }

    //4
    fun updateBook(isbn: String, bookDTO: BookDTO): BookDTO{
        if(books.containsKey(isbn)){
            books[isbn] = mappers.dtoToBook(bookDTO)
        } else {
            createBook(bookDTO)
        }
        return bookDTO
    }

    //5
    fun patchBook(isbn: String, fields: Map<String, Any>): BookDTO {
        val toPatch = books[isbn] ?: throw BookstoreExceptions.NotFoundException(isbn)

        val patched = toPatch.copy(
            title = fields["title"] as? String ?: toPatch.title,
            author = fields["author"] as? String ?: toPatch.author,
            price = fields["price"]?.toString()?.toDoubleOrNull() ?: toPatch.price,
            image = fields["image"] as? String ?: toPatch.image,
        )

        val patchedDto = mappers.bookToDto(patched)
        val check = validator.validate(patchedDto)

        if (check.isNotEmpty()) {
            throw ConstraintViolationException(check)
        }

        books[isbn] = mappers.dtoToBook(patchedDto)
        return patchedDto
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

    fun getSingleReview(isbn: String, reviewId: String): ReviewDTO {
        val reviews = reviews[isbn] ?: throw BookstoreExceptions.NotFoundException(isbn)
        val review = reviews.find{ it.id.toString() == reviewId } ?: throw BookstoreExceptions.NotFoundException(reviewId)
        return mappers.reviewToDto(review)

    }

    fun addReview(isbn: String, reviewDto: ReviewDTO): ReviewDTO {
        val book = reviews[isbn] ?: throw BookstoreExceptions.NotFoundException(isbn)
        val currUsername = SecurityContextHolder.getContext().authentication.name
        val review = mappers.dtoToReview(reviewDto, currUsername)
        book.add(review)
        reviews[isbn] = book
        return mappers.reviewToDto(review)
    }

    fun updateReview(isbn: String, reviewId: String, reviewDto: ReviewDTO) {
        val currUsername = SecurityContextHolder.getContext().authentication.name
        val reviews = reviews[isbn] ?: throw BookstoreExceptions.NotFoundException(isbn)
        val targetReview = UUID.fromString(reviewId)
        val index = reviews.indexOfFirst{ it.id == targetReview }

        if(index == -1)
            throw BookstoreExceptions.NotFoundException(reviewId)

        reviews[index] = mappers.dtoToReview(reviewDto, currUsername)
    }

    fun patchReview(isbn: String, reviewId: String, fields: Map<String, Any>) {
        val currUsername = SecurityContextHolder.getContext().authentication.name
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

        val patchedDto = mappers.reviewToDto(patched)
        val check = validator.validate(patchedDto)

        if (check.isNotEmpty()) {
            throw ConstraintViolationException(check)
        }

        reviews[index] = mappers.dtoToReview(patchedDto, currUsername)
    }

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