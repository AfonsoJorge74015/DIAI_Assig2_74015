package pt.unl.fct.iadi.bookstore.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    //1
    override fun getBooks(): ResponseEntity<List<BookDTO>> {
        val result = service.getBooks()
        return ResponseEntity.ok(result)
    }

    //2
    override fun addBook(bookDTO: BookDTO): ResponseEntity<Unit> {
        service.createBook(bookDTO)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{isbn}")
            .buildAndExpand(bookDTO.isbn)
            .toUri()
        return ResponseEntity.created(location).build()
    }

    //3
    override fun getBook(isbn: String): ResponseEntity<BookDTO> {
        val result = service.getBook(isbn)
        return ResponseEntity.ok(result)
    }

    //4
    override fun updateBook(isbn: String, bookDTO: BookDTO): ResponseEntity<*> {
        return try {
            service.getBook(isbn)
            service.updateBook(isbn, bookDTO)
            ResponseEntity.ok(bookDTO)
        } catch (e: BookstoreExceptions.NotFoundException) {
            addBook(bookDTO)
        }
    }

    //5
    override fun patchBook(isbn: String, fields: Map<String, Any>): ResponseEntity<BookDTO> {
        val patched = service.patchBook(isbn, fields)
        return ResponseEntity.ok(patched)
    }

    //6
    override fun deleteBook(isbn: String) : ResponseEntity<Unit> {
        service.deleteBook(isbn)
        return ResponseEntity.noContent().build()
    }

    //7
    override fun getReviews(isbn: String): ResponseEntity<List<ReviewDTO>> {
        val reviews = service.getReviews(isbn)
        return ResponseEntity.ok(reviews)
    }

    //8
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

    //9
    override fun updateReview(isbn: String, reviewId: String, reviewDto: ReviewDTO)
        : ResponseEntity<Unit> {

        service.updateReview(isbn, reviewId, reviewDto)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    //10
    override fun patchReview(isbn: String, reviewId: String, fields: Map<String, Any>)
        : ResponseEntity<Unit> {

        service.patchReview(isbn, reviewId, fields)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    //11
    override fun deleteReview(isbn: String, reviewId: String): ResponseEntity<Unit> {
        service.deleteReview(isbn, reviewId)
        return ResponseEntity.noContent().build()
    }


}