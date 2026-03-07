package pt.unl.fct.iadi.bookstore.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import pt.unl.fct.iadi.bookstore.service.exceptions.BookstoreExceptions
import pt.unl.fct.iadi.bookstore.utils.ErrorResponse

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleValidation(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            error = "VALIDATION_ERROR",
            message = e.message ?: "Validation error",
        )
        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler(BookstoreExceptions.AlreadyExistsException::class)
    fun handleAlreadyExists(e: BookstoreExceptions.AlreadyExistsException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            error = "ALREADY_EXISTS",
            message = e.message ?: "Item already exists"
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }

    @ExceptionHandler(BookstoreExceptions.NotFoundException::class)
    fun handleNotFound(e: BookstoreExceptions.NotFoundException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            error = "NOT_FOUND",
            message = e.message ?: "Item not found"
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }
}