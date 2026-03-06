package pt.unl.fct.iadi.bookstore.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.bookstore.domain.Book
import pt.unl.fct.iadi.bookstore.domain.Review
import java.util.concurrent.ConcurrentHashMap

@Service
class BookstoreService {
    private val books = ConcurrentHashMap<String, Book>()
    private val reviews = ConcurrentHashMap<String, List<Review>>()


}