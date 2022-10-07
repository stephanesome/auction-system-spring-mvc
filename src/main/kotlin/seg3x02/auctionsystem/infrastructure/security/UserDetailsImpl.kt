package seg3x02.auctionsystem.infrastructure.security

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import seg3x02.auctionsystem.infrastructure.security.credentials.User

class UserDetailsImpl(val id: Long, private val username: String,
                      @field:JsonIgnore private val password: String,
                      private val enabled: Boolean,
                      private val authorities: Collection<GrantedAuthority>) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }
}

fun build(user: User): UserDetailsImpl {
    val authorities = ArrayList<GrantedAuthority>()
    authorities.add(SimpleGrantedAuthority(user.role.name))
    return UserDetailsImpl(
        user.id,
        user.username,
        user.password,
        user.enabled,
        authorities)
}

