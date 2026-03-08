package pt.unl.fct.iadi.bookstore.controller

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpHeaders
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
        val locale = LocaleContextHolder.getLocale()

        // US3: Support localized messages (simple implementation)
        val message = if (locale.language == "pt") {
            "${e.message?.substringBefore(" ")} não encontrado"
        } else {
            e.message ?: "Item not found"
        }

        val response = ErrorResponse(
            error = "NOT_FOUND",
            message = message
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .header(HttpHeaders.CONTENT_LANGUAGE, locale.language) // US3 Requirement
            .body(response)
    }
}