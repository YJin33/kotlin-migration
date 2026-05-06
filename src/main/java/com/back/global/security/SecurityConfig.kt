package com.back.global.security


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authorization.SingleResultAuthorizationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customAuthenticationFilter: CustomAuthenticationFilter
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http {
            authorizeHttpRequests {
                authorize("/favicon.ico", SingleResultAuthorizationManager.permitAll())
                authorize("/h2-console/**", SingleResultAuthorizationManager.permitAll())
                authorize(HttpMethod.GET, "/api/*/posts", SingleResultAuthorizationManager.permitAll())
                authorize(HttpMethod.GET, "/api/*/posts/{id:\\\\d+}", SingleResultAuthorizationManager.permitAll())
                authorize(HttpMethod.GET, "/api/*/posts/{postId:\\\\d+}/comments",
                    SingleResultAuthorizationManager.permitAll()
                )
                authorize(HttpMethod.GET, "/api/*/posts/{postId:\\\\d+}/comments/{commentId:\\\\d+}",
                    SingleResultAuthorizationManager.permitAll()
                )
                authorize(HttpMethod.POST, "/api/v1/members/login", SingleResultAuthorizationManager.permitAll())
                authorize(HttpMethod.POST, "/api/v1/members/join", SingleResultAuthorizationManager.permitAll())
                authorize(HttpMethod.DELETE, "/api/v1/members/logout", SingleResultAuthorizationManager.permitAll())
                authorize("/api/*/adm/**", hasRole("ADMIN"))
                authorize("/api/*/**", authenticated)
                authorize(anyRequest, SingleResultAuthorizationManager.permitAll())
            }

            csrf { disable() }

            headers {
                frameOptions { sameOrigin = true }
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(customAuthenticationFilter)
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }

            exceptionHandling {
                authenticationEntryPoint = AuthenticationEntryPoint { _, response, _ ->
                    response.contentType =
                        "application/json; charset=UTF-8"
                    response.status = 401
                    response.writer.write(
                        """
                            {
                                "resultCode": "401-1",
                                "msg": "로그인 후 이용해주세요."
                            }
                            """.trimIndent()
                    )
                }

                accessDeniedHandler = AccessDeniedHandler { _, response, _ ->
                    response.contentType =
                        "application/json; charset=UTF-8"
                    response.status = 403
                    response.writer.write(
                        """
                            {
                                "resultCode": "403-1",
                                "msg": "권한이 없습니다."
                            }
                        """.trimIndent()
                    )
                }
            }
        }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins =
                listOf("https://cdpn.io", "http://localhost:3000")
            allowedMethods =
                listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/api/**", configuration)
        }
    }
}