package pt.unl.fct.iadi.bookstore.controller

import jakarta.validation.ConstraintViolationException
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import pt.unl.fct.iadi.bookstore.service.exceptions.BookstoreExceptions
import pt.unl.fct.iadi.bookstore.utils.ErrorResponse
import java.util.Locale

@RestControllerAdvice
class GlobalExceptionHandler(
    private val messageSource: MessageSource
) {

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

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            error = "BAD_REQUEST",
            message = e.message ?: "Validation error",
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    @ExceptionHandler(BookstoreExceptions.NotFoundException::class)
    fun handleNotFound(e: BookstoreExceptions.NotFoundException): ResponseEntity<ErrorResponse> {
        val locale = LocaleContextHolder.getLocale()

        val targetLocale = if (locale.language == "pt") {
            Locale.forLanguageTag("pt")
        } else {
            Locale.ENGLISH
        }

        val locMessage = messageSource.getMessage("error.notfound",
            null, "Item not found",targetLocale)

        val response = ErrorResponse(
            error = "NOT_FOUND",
            message = locMessage!!
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .header(HttpHeaders.CONTENT_LANGUAGE, locale.language)
            .body(response)
    }
}