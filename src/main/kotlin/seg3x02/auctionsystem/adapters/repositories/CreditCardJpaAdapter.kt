package seg3x02.auctionsystem.adapters.repositories

import org.mapstruct.factory.Mappers
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import seg3x02.auctionsystem.adapters.repositories.converters.CreditCardJpaConverter
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import seg3x02.auctionsystem.infrastructure.jpa.dao.CreditCardJpaRepository

@Component
@CacheConfig(cacheNames=["creditCards"])
class CreditCardJpaAdapter(private val creditCardRepository: CreditCardJpaRepository): CreditCardRepository {
    private val converter = Mappers.getMapper(CreditCardJpaConverter::class.java)

    @CachePut(key = "#creditCard.number")
    override fun save(creditCard: CreditCard): CreditCard {
        val creditCardJpa = converter.convertToJpa(creditCard)
        creditCardRepository.save(creditCardJpa)
        return creditCard
    }

    @Cacheable(key = "#ccNumber")
    override fun find(ccNumber: String): CreditCard? {
        val creditCardJpa = creditCardRepository.findByIdOrNull(ccNumber)
        return creditCardJpa?.let { converter.convertToModel(it) }
    }
}
