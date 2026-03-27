package pt.unl.fct.iadi.bookstore.security

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.security.SecuritySchemes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pt.unl.fct.iadi.bookstore.security.filters.ApiTokenFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@SecuritySchemes(
    SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic",
    ),
    SecurityScheme(
        name = "apiToken",
        type = SecuritySchemeType.APIKEY,
        `in` = SecuritySchemeIn.HEADER,
        paramName = "X-Api-Token",
    )
)
class SecurityConfig(
    private val apiTokenFilter: ApiTokenFilter
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun userDetailsService(encoder: PasswordEncoder) = InMemoryUserDetailsManager(
        User.withUsername("editor1")
            .password(encoder.encode("editor1pass"))
            .roles("EDITOR")
            .build(),
        User.withUsername("editor2")
            .password(encoder.encode("editor2pass"))
            .roles("EDITOR")
            .build(),
        User.withUsername("admin")
            .password(encoder.encode("adminpass"))
            .roles("ADMIN")
            .build(),
    )

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .addFilterBefore(
                apiTokenFilter,
                UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic {}
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint { _, response, _
                    ->
                    response.status = 401
                    response.contentType = "application/json"
                    response.writer.write("""{"error":"UNAUTHORIZED","message":"Missing or invalid X-Api-Token"}""")
                }

                exceptions.accessDeniedHandler { _, response, _
                    ->
                    response.status = 403
                    response.contentType = "application/json"
                    response.writer.write("""{"error":"FORBIDDEN","message":"You don't have permission to access this resource"}""")
                }
            }
        return http.build()
    }
}