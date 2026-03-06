package pt.unl.fct.iadi.bookstore.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.domain.Review

@RequestMapping("/bookstore")
interface BookstoreAPI {

    //1
    @GetMapping("/books")
    fun getBooks() : List<Book>

    //2
    @PostMapping("/books")
    fun addBook(@RequestBody book: Book) : Book

    //3
    @GetMapping("/books/{id}")
    fun getBook(@PathVariable id : String) : Book

    //4
    @PutMapping("/books/{id}")
    fun upsertBook(@PathVariable id: String, @RequestBody book: Book) : Book

    //5
    @PatchMapping("/books/{id}")
    fun patchBook(@PathVariable id: String, @RequestBody fields: Map<String, Any>) : Book

    //6
    @DeleteMapping("/books/{id}")
    fun deleteBook(@PathVariable id: String)

    //7
    @GetMapping("/books/{id}/reviews")
    fun getReviews(@PathVariable id: String) : List<Review>

    //8
    @PostMapping("/books/{id}/reviews")
    fun addReview(@PathVariable id: String, @RequestBody review: Review) : Book

    //9
    @PutMapping("/books/{id}/reviews/{reviewId}")
    fun upsertReview(@PathVariable id: String, @PathVariable reviewId: String, @RequestBody review: Review) : Book

    //10
    @PatchMapping("/books/{id}/reviews/{reviewId}")
    fun patchReview(@PathVariable id: String, @PathVariable reviewId: String, @RequestBody fields: Map<String, Any>) : Book

    //11
    @DeleteMapping("/books/{id}/reviews/{reviewId}")
    fun deleteReview(@PathVariable id: String, @PathVariable reviewId: String)
}