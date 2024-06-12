package org.hse.security.service

import org.hse.security.model.Customer
import org.hse.security.repository.CustomerRepository
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.io.encoding.ExperimentalEncodingApi

@Service
@ExperimentalEncodingApi
class CustomerService(
    private val customerDetailsService: CustomerDetailsService,
    private val customerRepository: CustomerRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun registerCustomer(name: String, email: String, password: String): HttpEntity<String> {
        if (customerRepository.findByName(name) != null) {
            throw RuntimeException("User already exists.")
        }

        if (customerRepository.findByEmail(email) != null) {
            throw RuntimeException("This email is already in use.")
        }

        val now = LocalDateTime.now()

        val encodedPassword = passwordEncoder.encode(password)
        customerRepository.save(
            Customer(
                name = name,
                email = email,
                password = encodedPassword,
                created = now
            )
        )

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.")
    }

    fun loginCustomer(username: String, password: String): ResponseEntity<String> {
        return try {
            val customerDetails: UserDetails = customerDetailsService.loadUserByUsername(username)

            if (!passwordEncoder.matches(password, customerDetails.password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.")
            }

            val jwt = jwtService.generateToken(customerDetails)
            ResponseEntity.ok(jwt)
        } catch (e: UsernameNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("${e.message}")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login.")
        }
    }

}