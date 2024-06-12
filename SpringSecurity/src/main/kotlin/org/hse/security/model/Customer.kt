package org.hse.security.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.relational.core.mapping.Column
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.UUID

@Table("users")
class Customer(
    @Id
    @Column("id")
    var id: UUID? = null,

    @Column("name")
    private var name: String,

    @Column("email")
    private var email: String,

    @Column("password")
    private var password: String,

    @Column("created")
    private var created: LocalDateTime? = null

) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    fun setName(name: String) {
        this.name = name
    }

    override fun getUsername() = name

    fun setPassword(password: String) {
        this.password = password
    }

    override fun getPassword() = password

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}