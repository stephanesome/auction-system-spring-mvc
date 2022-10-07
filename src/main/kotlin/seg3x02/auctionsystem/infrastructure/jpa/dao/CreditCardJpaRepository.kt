package seg3x02.auctionsystem.infrastructure.jpa.dao

import org.springframework.data.repository.CrudRepository
import seg3x02.auctionsystem.infrastructure.jpa.entities.user.creditCard.CreditCardJpaEntity

interface CreditCardJpaRepository: CrudRepository<CreditCardJpaEntity, String> {
}
