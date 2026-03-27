package pt.unl.fct.iadi.bookstore.service.exceptions

sealed class BookstoreExceptions(
    message: String,
) : RuntimeException(message) {

    class AlreadyExistsException(itemId: String)
        : BookstoreExceptions("$itemId already exists")

    class NotFoundException(itemId: String)
        : BookstoreExceptions("$itemId not found")

}