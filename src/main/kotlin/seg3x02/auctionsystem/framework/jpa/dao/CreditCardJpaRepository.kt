package seg3x02.auctionsystem.framework.jpa.dao

import org.springframework.data.repository.CrudRepository
import seg3x02.auctionsystem.framework.jpa.entities.user.creditCard.CreditCardJpaEntity
import java.util.*

interface CreditCardJpaRepository: CrudRepository<CreditCardJpaEntity, Number> {
}
