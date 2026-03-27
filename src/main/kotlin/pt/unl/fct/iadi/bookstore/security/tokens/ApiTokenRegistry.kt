package pt.unl.fct.iadi.bookstore.security.tokens

import org.springframework.stereotype.Component

@Component
class ApiTokenRegistry {
    val tokenToApp = mapOf(
        "catalog-app" to "token-catalog-abc123",
        "mobile-app" to "token-mobile-def456",
        "web-app" to "token-web-ghi789"
    )
}