package pt.unl.fct.iadi.bookstore.service.exceptions

sealed class BookstoreExceptions(
    message: String,
) : RuntimeException(message) {

    class AlreadyExistsException(isbn: String)
        : BookstoreExceptions("$isbn already exists")

    class NotFoundException(isbn: String)
        : BookstoreExceptions("$isbn not found")

}