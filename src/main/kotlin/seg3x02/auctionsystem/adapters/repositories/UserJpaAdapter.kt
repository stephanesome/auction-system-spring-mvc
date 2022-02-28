package seg3x02.auctionsystem.adapters.repositories

import org.mapstruct.factory.Mappers
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import seg3x02.auctionsystem.adapters.repositories.converters.AccountJpaConverter
import seg3x02.auctionsystem.domain.user.core.account.UserAccount
import seg3x02.auctionsystem.domain.user.repositories.UserRepository
import seg3x02.auctionsystem.framework.jpa.dao.UserJpaRepository
import java.util.*

@Component
@CacheConfig(cacheNames=["users"])
class UserJpaAdapter(private val userRepository: UserJpaRepository): UserRepository {
    private val converter = Mappers.getMapper(AccountJpaConverter::class.java)

    @Cacheable(key = "#id")
    @Transactional
    override fun find(id: UUID): UserAccount? {
        val accountJpa = userRepository.findByIdOrNull(id)
        return accountJpa?.let { converter.convertToModel(it) }
    }

    @CachePut(key = "#account.id")
    override fun save(account: UserAccount): UserAccount {
        val accountJpa = converter.convertToJpa(account)
        userRepository.save(accountJpa)
        return account
    }
}
