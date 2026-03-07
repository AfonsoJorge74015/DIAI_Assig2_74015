package pt.unl.fct.iadi.bookstore.controller

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

@RequestMapping("/bookstore")
interface BookstoreAPI {

    //1
    @GetMapping("/books")
    fun getBooks() : ResponseEntity<List<BookDTO>>

    //2
    @PostMapping("/books")
    fun addBook(@RequestBody bookDto: BookDTO) : ResponseEntity<BookDTO>

    //3
    @GetMapping("/books/{isbn}")
    fun getBook(@PathVariable isbn : String) : ResponseEntity<BookDTO>

    //4
    @PutMapping("/books/{isbn}")
    fun updateBook(@PathVariable isbn: String, @RequestBody bookDto: BookDTO) : ResponseEntity<BookDTO>

    //5
    @PatchMapping("/books/{isbn}")
    fun patchBook(@PathVariable isbn: String, @RequestBody fields: Map<String, Any>) : ResponseEntity<BookDTO>

    //6
    @DeleteMapping("/books/{isbn}")
    fun deleteBook(@PathVariable isbn: String) : ResponseEntity<Unit>

    //7
    @GetMapping("/books/{isbn}/reviews")
    fun getReviews(@PathVariable isbn: String) : ResponseEntity<List<ReviewDTO>>

    //8
    @PostMapping("/books/{isbn}/reviews")
    fun addReview(@PathVariable isbn: String, @RequestBody reviewDto: ReviewDTO) : ResponseEntity<ReviewDTO>

    //9
    @PutMapping("/books/{isbn}/reviews/{reviewId}")
    fun updateReview(@PathVariable isbn: String, @PathVariable reviewId: String, @RequestBody reviewDto: ReviewDTO) : ResponseEntity<ReviewDTO>

    //10
    @PatchMapping("/books/{isbn}/reviews/{reviewId}")
    fun patchReview(@PathVariable isbn: String, @PathVariable reviewId: String, @RequestBody fields: Map<String, Any>) : ResponseEntity<ReviewDTO>

    //11
    @DeleteMapping("/books/{isbn}/reviews/{reviewId}")
    fun deleteReview(@PathVariable isbn: String, @PathVariable reviewId: String): ResponseEntity<Unit>
}