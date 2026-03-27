package pt.unl.fct.iadi.bookstore.utils

import org.springframework.stereotype.Component
import pt.unl.fct.iadi.bookstore.controller.dto.CreateBookRequest
import pt.unl.fct.iadi.bookstore.controller.dto.ReviewDTO
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.domain.Review

@Component
class Mappers {

    //To DTO
    fun bookToDto(book: Book) =
        CreateBookRequest(book.isbn,
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
    fun dtoToBook(createBookRequest: CreateBookRequest) =
        Book(createBookRequest.isbn,
            createBookRequest.title,
            createBookRequest.author,
            createBookRequest.price,
            createBookRequest.image)

    fun dtoToReview(reviewDto: ReviewDTO) =
        Review(
            author = reviewDto.author,
            rating = reviewDto.rating,
            comment = reviewDto.comment)
}