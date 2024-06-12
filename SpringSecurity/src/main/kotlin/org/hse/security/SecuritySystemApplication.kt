package org.hse.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SecuritySystemApplication

fun main(args: Array<String>) {
    runApplication<SecuritySystemApplication>(*args)
}
