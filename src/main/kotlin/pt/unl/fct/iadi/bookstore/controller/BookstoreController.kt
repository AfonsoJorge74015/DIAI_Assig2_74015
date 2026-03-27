package pt.unl.fct.iadi.bookstore.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import pt.unl.fct.iadi.bookstore.controller.dto.BookDTO
import pt.unl.fct.iadi.bookstore.controller.dto.ReviewDTO
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.service.BookstoreService
import pt.unl.fct.iadi.bookstore.service.exceptions.BookstoreExceptions

@RestController
class BookstoreController(
    private val service: BookstoreService)
    : BookstoreAPI {

    override fun getBooks(): ResponseEntity<List<BookDTO>> {
        val result = service.getBooks()
        return ResponseEntity.ok(result)
    }

    override fun addBook(bookDTO: BookDTO): ResponseEntity<Unit> {
        service.createBook(bookDTO)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{isbn}")
            .buildAndExpand(bookDTO.isbn)
            .toUri()
        return ResponseEntity.created(location).build()
    }

    override fun getBook(isbn: String): ResponseEntity<BookDTO> {
        val result = service.getBook(isbn)
        return ResponseEntity.ok(result)
    }

    override fun updateBook(isbn: String, bookDTO: BookDTO): ResponseEntity<*> {
        return try {
            service.getBook(isbn)
            service.updateBook(isbn, bookDTO)
            ResponseEntity.ok(bookDTO)
        } catch (e: BookstoreExceptions.NotFoundException) {
            addBook(bookDTO)
        }
    }

    override fun patchBook(isbn: String, fields: Map<String, Any>): ResponseEntity<BookDTO> {
        val patched = service.patchBook(isbn, fields)
        return ResponseEntity.ok(patched)
    }

    override fun deleteBook(isbn: String) : ResponseEntity<Unit> {
        service.deleteBook(isbn)
        return ResponseEntity.noContent().build()
    }

    override fun getReviews(isbn: String): ResponseEntity<List<ReviewDTO>> {
        val reviews = service.getReviews(isbn)
        return ResponseEntity.ok(reviews)
    }

    override fun getSingleReview(@PathVariable isbn: String, @PathVariable reviewId: String): ResponseEntity<ReviewDTO>{
        val review = service.getSingleReview(isbn, reviewId)
        return ResponseEntity.ok(review)
    }

    override fun addReview(isbn: String, reviewDto: ReviewDTO)
        : ResponseEntity<Unit> {
        val review = service.addReview(isbn, reviewDto)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{reviewId}")
            .buildAndExpand(review.id)
            .toUri()
        return ResponseEntity.created(location).build()
    }

    override fun updateReview(isbn: String, reviewId: String, reviewDto: ReviewDTO)
        : ResponseEntity<Unit> {

        service.updateReview(isbn, reviewId, reviewDto)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    override fun patchReview(isbn: String, reviewId: String, fields: Map<String, Any>)
        : ResponseEntity<Unit> {

        service.patchReview(isbn, reviewId, fields)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    override fun deleteReview(isbn: String, reviewId: String): ResponseEntity<Unit> {
        service.deleteReview(isbn, reviewId)
        return ResponseEntity.noContent().build()
    }


}