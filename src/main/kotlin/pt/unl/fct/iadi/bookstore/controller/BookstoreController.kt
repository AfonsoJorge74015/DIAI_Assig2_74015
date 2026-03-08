package pt.unl.fct.iadi.bookstore.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import pt.unl.fct.iadi.bookstore.controller.dto.BookDTO
import pt.unl.fct.iadi.bookstore.controller.dto.ReviewDTO
import pt.unl.fct.iadi.bookstore.service.BookstoreService

@Controller
class BookstoreController(
    private val service: BookstoreService)
    : BookstoreAPI {

    //1
    override fun getBooks(): ResponseEntity<List<BookDTO>> {
        val result = service.getBooks()
        return ResponseEntity.ok(result)
    }

    //2
    override fun addBook(bookDto: BookDTO): ResponseEntity<BookDTO> {
        val created = service.createBook(bookDto)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{isbn}")
            .buildAndExpand(bookDto.isbn)
            .toUri()
        return ResponseEntity.created(location).body(created)
    }

    //3
    override fun getBook(isbn: String): ResponseEntity<BookDTO> {
        val result = service.getBook(isbn)
        return ResponseEntity.ok(result)
    }

    //4
    override fun updateBook(isbn: String, bookDto: BookDTO): ResponseEntity<BookDTO> {
        val updated = service.updateBook(isbn, bookDto)
        return ResponseEntity.ok(updated)
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
        : ResponseEntity<ReviewDTO> {
        val review = service.addReview(isbn, reviewDto)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{reviewId}")
            .buildAndExpand(review.id)
            .toUri()
        return ResponseEntity.created(location).body(review)
    }

    //9
    override fun updateReview(isbn: String, reviewId: String, reviewDto: ReviewDTO)
        : ResponseEntity<ReviewDTO> {

        val review = service.updateReview(isbn, reviewId, reviewDto)
        return ResponseEntity.ok(review)
    }

    //10
    override fun patchReview(isbn: String, reviewId: String, fields: Map<String, Any>)
        : ResponseEntity<ReviewDTO> {

        val review = service.patchReview(isbn, reviewId, fields)
        return ResponseEntity.ok(review)
    }

    //11
    override fun deleteReview(isbn: String, reviewId: String): ResponseEntity<Unit> {
        service.deleteReview(isbn, reviewId)
        return ResponseEntity.noContent().build()
    }


}