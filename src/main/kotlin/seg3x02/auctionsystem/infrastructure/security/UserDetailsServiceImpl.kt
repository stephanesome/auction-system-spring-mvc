package seg3x02.auctionsystem.infrastructure.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import seg3x02.auctionsystem.infrastructure.jpa.dao.UserJpaRepository
import seg3x02.auctionsystem.infrastructure.security.credentials.User
import jakarta.transaction.Transactional

@Service
class UserDetailsServiceImpl(val userRepository: UserJpaRepository): UserDetailsService {

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User username: $username not found") }
        return build(user)
    }
}
