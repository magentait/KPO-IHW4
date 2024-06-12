package org.hse.security.repository

import org.hse.security.model.Customer
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface CustomerRepository : CrudRepository<Customer, UUID> {

    fun findByName(name: String): Customer?

    fun findByEmail(email: String): Customer?
}
