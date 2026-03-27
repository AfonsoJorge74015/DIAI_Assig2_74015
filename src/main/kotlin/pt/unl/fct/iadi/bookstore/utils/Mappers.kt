package pt.unl.fct.iadi.bookstore.utils

import org.springframework.stereotype.Component
import pt.unl.fct.iadi.bookstore.controller.dto.BookDTO
import pt.unl.fct.iadi.bookstore.controller.dto.ReviewDTO
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.domain.Review

@Component
class Mappers {

    //To DTO
    fun bookToDto(book: Book) =
        BookDTO(book.isbn,
            book.title,
            book.author,
            book.price,
            book.image)

    fun reviewToDto(review: Review) =
        ReviewDTO(review.id,
            review.author,
            review.rating,
            review.comment)


    //To domain
    fun dtoToBook(bookDTO: BookDTO) =
        Book(bookDTO.isbn,
            bookDTO.title,
            bookDTO.author,
            bookDTO.price,
            bookDTO.image)

    fun dtoToReview(reviewDto: ReviewDTO, author: String, currId: Long) =
        Review(
            id = currId,
            author = author,
            rating = reviewDto.rating,
            comment = reviewDto.comment)
}