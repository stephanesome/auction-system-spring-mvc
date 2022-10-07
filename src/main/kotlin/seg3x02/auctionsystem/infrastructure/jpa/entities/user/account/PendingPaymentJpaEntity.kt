package seg3x02.auctionsystem.infrastructure.jpa.entities.user.account

import java.math.BigDecimal
import javax.persistence.Embeddable

@Embeddable
class PendingPaymentJpaEntity(var amount: BigDecimal)
