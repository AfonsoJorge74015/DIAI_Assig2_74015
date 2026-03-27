package pt.unl.fct.iadi.bookstore.security.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import pt.unl.fct.iadi.bookstore.security.tokens.ApiTokenRegistry

@Component
class ApiTokenFilter(
    private val registry: ApiTokenRegistry
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("X-Api-Token")
        val appName = registry.tokenToApp[token]

        if (appName == "unknown") {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write("""{"error": "UNAUTHORIZED", "message":"Missing or invalid X-Api-Token"}""")
            return
        }

        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")
    }
}