package seg3x02.auctionsystem.infrastructure.jpa.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import seg3x02.auctionsystem.infrastructure.security.credentials.User
import java.util.*

interface UserJpaRepository: CrudRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>

    fun existsByUsername(username: String): Boolean

    @Transactional
    fun deleteByUsername(userName: String)
}
