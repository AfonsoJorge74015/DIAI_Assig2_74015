package pt.unl.fct.iadi.bookstore.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import pt.unl.fct.iadi.bookstore.controller.dto.BookDTO
import pt.unl.fct.iadi.bookstore.controller.dto.ReviewDTO
import pt.unl.fct.iadi.bookstore.service.BookstoreService
import pt.unl.fct.iadi.bookstore.utils.ErrorResponse

@RestController
@RequestMapping("/bookstore")  // ← IMPORTANTE: Adiciona isto aqui também!
@Tag(name = "Bookstore", description = "Operations related to books and reviews")
class BookstoreController(
    private val service: BookstoreService
) : BookstoreAPI {

    //1
    @Operation(summary = "Get all books", description = "Returns a list of all books currently in the store.")
    @GetMapping("/books")  // ← Repete a anotação aqui!
    override fun getBooks(): ResponseEntity<List<BookDTO>> {
        val result = service.getBooks()
        return ResponseEntity.ok(result)
    }

    //2
    @Operation(summary = "Create a new book", description = "Adds a new book to the store. Fails if the ISBN already exists.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Book successfully created"),
        ApiResponse(responseCode = "400", description = "Validation error (e.g., negative price)", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "409", description = "Conflict: A book with this ISBN already exists", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PostMapping("/books")  // ← Repete a anotação aqui!
    override fun addBook(@RequestBody bookDto: BookDTO): ResponseEntity<BookDTO> {
        val created = service.createBook(bookDto)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{isbn}")
            .buildAndExpand(bookDto.isbn)
            .toUri()
        return ResponseEntity.created(location).body(created)
    }

    //3
    @Operation(summary = "Get a specific book", description = "Retrieves a book's details by its ISBN.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Book found"),
        ApiResponse(responseCode = "404", description = "Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/books/{isbn}")  // ← Repete a anotação aqui!
    override fun getBook(@PathVariable isbn: String): ResponseEntity<BookDTO> {
        val result = service.getBook(isbn)
        return ResponseEntity.ok(result)
    }

    //4
    @Operation(summary = "Replace or create a book", description = "Fully replaces a book's info. If the ISBN doesn't exist, it creates the book (upsert).")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Book updated"),
        ApiResponse(responseCode = "201", description = "Book created"),
        ApiResponse(responseCode = "400", description = "Validation error", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PutMapping("/books/{isbn}")  // ← Repete a anotação aqui!
    override fun updateBook(@PathVariable isbn: String, @RequestBody bookDto: BookDTO): ResponseEntity<BookDTO> {
        val updated = service.updateBook(isbn, bookDto)
        return ResponseEntity.ok(updated)
    }

    //5
    @Operation(summary = "Partially update a book", description = "Updates only the provided fields of a book.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Book updated"),
        ApiResponse(responseCode = "400", description = "Invalid field values", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", description = "Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PatchMapping("/books/{isbn}")  // ← Repete a anotação aqui!
    override fun patchBook(@PathVariable isbn: String, @RequestBody fields: Map<String, Any>): ResponseEntity<BookDTO> {
        val patched = service.patchBook(isbn, fields)
        return ResponseEntity.ok(patched)
    }

    //6
    @Operation(summary = "Delete a book", description = "Removes a book and all its associated reviews from the store.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Book deleted"),
        ApiResponse(responseCode = "404", description = "Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @DeleteMapping("/books/{isbn}")  // ← Repete a anotação aqui!
    override fun deleteBook(@PathVariable isbn: String): ResponseEntity<Unit> {
        service.deleteBook(isbn)
        return ResponseEntity.noContent().build()
    }

    //7
    @Operation(summary = "Get reviews for a book", description = "Retrieves all reviews associated with a specific book ISBN.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of reviews returned"),
        ApiResponse(responseCode = "404", description = "Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/books/{isbn}/reviews")  // ← Repete a anotação aqui!
    override fun getReviews(@PathVariable isbn: String): ResponseEntity<List<ReviewDTO>> {
        val reviews = service.getReviews(isbn)
        return ResponseEntity.ok(reviews)
    }

    //8
    @Operation(summary = "Add a review", description = "Creates a new review for a book. The review ID is generated by the server.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Review created"),
        ApiResponse(responseCode = "400", description = "Validation error (e.g., rating not 1-5)", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", description = "Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PostMapping("/books/{isbn}/reviews")  // ← Repete a anotação aqui!
    override fun addReview(@PathVariable isbn: String, @RequestBody reviewDto: ReviewDTO): ResponseEntity<ReviewDTO> {
        val review = service.addReview(isbn, reviewDto)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{reviewId}")
            .buildAndExpand(review.id)
            .toUri()
        return ResponseEntity.created(location).body(review)
    }

    //9
    @Operation(summary = "Replace a review", description = "Fully replaces a review's content. Requires the unique review UUID.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Review updated"),
        ApiResponse(responseCode = "400", description = "Validation error", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", description = "Review or Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PutMapping("/books/{isbn}/reviews/{reviewId}")  // ← Repete a anotação aqui!
    override fun updateReview(@PathVariable isbn: String, @PathVariable reviewId: String, @RequestBody reviewDto: ReviewDTO): ResponseEntity<ReviewDTO> {
        val review = service.updateReview(isbn, reviewId, reviewDto)
        return ResponseEntity.ok(review)
    }

    //10
    @Operation(summary = "Partially update a review", description = "Updates specific fields (rating or comment) of a review.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Review patched"),
        ApiResponse(responseCode = "400", description = "Validation error", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", description = "Review or Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PatchMapping("/books/{isbn}/reviews/{reviewId}")  // ← Repete a anotação aqui!
    override fun patchReview(@PathVariable isbn: String, @PathVariable reviewId: String, @RequestBody fields: Map<String, Any>): ResponseEntity<ReviewDTO> {
        val review = service.patchReview(isbn, reviewId, fields)
        return ResponseEntity.ok(review)
    }

    //11
    @Operation(summary = "Delete a review", description = "Removes a specific review by its UUID.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Review deleted"),
        ApiResponse(responseCode = "404", description = "Review or Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @DeleteMapping("/books/{isbn}/reviews/{reviewId}")  // ← Repete a anotação aqui!
    override fun deleteReview(@PathVariable isbn: String, @PathVariable reviewId: String): ResponseEntity<Unit> {
        service.deleteReview(isbn, reviewId)
        return ResponseEntity.noContent().build()
    }
}