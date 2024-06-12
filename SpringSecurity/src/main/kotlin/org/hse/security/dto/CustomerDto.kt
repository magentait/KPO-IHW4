package org.hse.security.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class UserRegistrationRequest(
    @field:NotBlank(message = "Name cannot be blank.")
    val name: String,

    @field:Email(message = "Email is not valid.")
    @field:NotBlank(message = "Email cannot be blank.")
    val email: String,

    @field:NotBlank(message = "Password cannot be blank.")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character."
    )
    val password: String
)

data class UserLoginRequest(
    @field:NotBlank(message = "Username is mandatory.")
    val name: String,

    @field:NotBlank(message = "Password is mandatory.")
    val password: String
)
