package pt.unl.fct.iadi.bookstore.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import pt.unl.fct.iadi.bookstore.controller.dto.BookDTO
import pt.unl.fct.iadi.bookstore.controller.dto.ReviewDTO
import pt.unl.fct.iadi.bookstore.utils.ErrorResponse

@Tag(name = "Bookstore", description = "Operations related to books and reviews")
interface BookstoreAPI {

    //1
    @Operation(summary = "Get all books", description = "Returns a list of all books currently in the store.")    @GetMapping("/books")
    fun getBooks() : ResponseEntity<List<BookDTO>>

    //2
    @Operation(summary = "Create a new book", description = "Adds a new book to the store. Fails if the ISBN already exists.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Book successfully created"),
        ApiResponse(responseCode = "400", description = "Validation error (e.g., negative price)",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "409", description = "Conflict: A book with this ISBN already exists",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PostMapping("/books")
    fun addBook(@RequestBody bookDto: BookDTO) : ResponseEntity<BookDTO>

    //3
    @Operation(summary = "Get a specific book", description = "Retrieves a book's details by its ISBN.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Book found", content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = BookDTO::class))]),
        ApiResponse(responseCode = "404", description = "Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/books/{isbn}")
    fun getBook(@PathVariable isbn : String) : ResponseEntity<BookDTO>

    //4
    @Operation(summary = "Replace or create a book", description = "Fully replaces a book's info. If the ISBN doesn't exist, it creates the book (upsert).")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Book updated"),
        ApiResponse(responseCode = "201", description = "Book created"),
        ApiResponse(responseCode = "400", description = "Validation error", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PutMapping("/books/{isbn}")
    fun updateBook(@PathVariable isbn: String, @RequestBody bookDto: BookDTO) : ResponseEntity<BookDTO>

    //5
    @Operation(summary = "Partially update a book", description = "Updates only the provided fields of a book.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Book updated"),
        ApiResponse(responseCode = "400", description = "Invalid field values", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", description = "Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PatchMapping("/books/{isbn}")
    fun patchBook(@PathVariable isbn: String, @RequestBody fields: Map<String, Any>) : ResponseEntity<BookDTO>

    //6
    @Operation(summary = "Delete a book", description = "Removes a book and all its associated reviews from the store.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Book deleted"),
        ApiResponse(responseCode = "404", description = "Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @DeleteMapping("/books/{isbn}")
    fun deleteBook(@PathVariable isbn: String) : ResponseEntity<Unit>

    //7
    @Operation(summary = "Get reviews for a book", description = "Retrieves all reviews associated with a specific book ISBN.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of reviews returned"),
        ApiResponse(responseCode = "404", description = "Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @GetMapping("/books/{isbn}/reviews")
    fun getReviews(@PathVariable isbn: String) : ResponseEntity<List<ReviewDTO>>

    //8
    @Operation(summary = "Add a review", description = "Creates a new review for a book. The review ID is generated by the server.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Review created"),
        ApiResponse(responseCode = "400", description = "Validation error (e.g., rating not 1-5)", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", description = "Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PostMapping("/books/{isbn}/reviews")
    fun addReview(@PathVariable isbn: String, @RequestBody reviewDto: ReviewDTO) : ResponseEntity<ReviewDTO>

    //9
    @Operation(summary = "Replace a review", description = "Fully replaces a review's content. Requires the unique review UUID.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Review updated"),
        ApiResponse(responseCode = "400", description = "Validation error", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", description = "Review or Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PutMapping("/books/{isbn}/reviews/{reviewId}")
    fun updateReview(@PathVariable isbn: String, @PathVariable reviewId: String, @RequestBody reviewDto: ReviewDTO) : ResponseEntity<ReviewDTO>

    //10
    @Operation(summary = "Partially update a review", description = "Updates specific fields (rating or comment) of a review.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Review patched"),
        ApiResponse(responseCode = "400", description = "Validation error", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ApiResponse(responseCode = "404", description = "Review or Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @PatchMapping("/books/{isbn}/reviews/{reviewId}")
    fun patchReview(@PathVariable isbn: String, @PathVariable reviewId: String, @RequestBody fields: Map<String, Any>) : ResponseEntity<ReviewDTO>

    //11
    @Operation(summary = "Delete a review", description = "Removes a specific review by its UUID.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Review deleted"),
        ApiResponse(responseCode = "404", description = "Review or Book not found", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
    ])
    @DeleteMapping("/books/{isbn}/reviews/{reviewId}")
    fun deleteReview(@PathVariable isbn: String, @PathVariable reviewId: String): ResponseEntity<Unit>
}