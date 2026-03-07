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
            review.rating,
            review.comment)


    //To domain
    fun dtoToBook(bookDto: BookDTO) =
        Book(bookDto.isbn,
            bookDto.title,
            bookDto.author,
            bookDto.price,
            bookDto.image)

    fun dtoToReview(reviewDto: ReviewDTO,) =
        Review(
            id = reviewDto.id,
            rating = reviewDto.rating,
            comment = reviewDto.comment)
}