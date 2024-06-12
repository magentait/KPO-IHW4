package org.hse.security.controller

import org.hse.security.dto.UserRegistrationRequest
import org.hse.security.dto.UserLoginRequest
import org.hse.security.service.CustomerService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*
import kotlin.io.encoding.ExperimentalEncodingApi

@RestController
@RequestMapping
@ExperimentalEncodingApi
class AuthController(
    private val customerService: CustomerService
) {

    val emailRegex =
        Regex("^\\S+@\\S+\\.\\S+\$")
    val passwordRegex =
        Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/register")
    fun register(): String {
        return "register"
    }

    @PostMapping("register")
    fun register(
        @RequestBody request: UserRegistrationRequest
    ): HttpEntity<String> {
        return try {
            logger.info("Registering user with name: ${request.name}, email: ${request.email}")

            // Проверка валидности email
            if (!emailRegex.matches(request.email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email!")
            }

            // Проверка валидности пароля
            if (!passwordRegex.matches(request.password)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password! Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
            }

            customerService.registerCustomer(request.name, request.email, request.password)
        } catch (e: Exception) {
            logger.error("Registration failed for user with name: ${request.name}, email: ${request.email}", e)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("${e.message}")
        }
    }

    @PostMapping("login")
    fun login(
        @RequestBody request: UserLoginRequest
    ): ResponseEntity<String> {
        return try {
            customerService.loginCustomer(request.name, request.password)
        } catch (e: UsernameNotFoundException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("${e.message}")
        }
    }

    @GetMapping("info")
    fun getUserInfo() =
        "Username: ${SecurityContextHolder.getContext().authentication.name}\n"

    companion object {
        private val logger = LoggerFactory.getLogger(AuthController::class.java)
    }
}