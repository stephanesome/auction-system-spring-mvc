package seg3x02.auctionsystem.infrastructure.jpa.entities.user.account

import java.math.BigDecimal
import jakarta.persistence.Embeddable

@Embeddable
class PendingPaymentJpaEntity(var amount: BigDecimal)
