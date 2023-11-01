package seg3x02.auctionsystem.infrastructure.jpa.entities.auction

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity()
@Table(name="BIDS")
data class BidJpaEntity(@Id val id: UUID,
                        val amount: BigDecimal,
                        val date: LocalDateTime,
                        val buyer: String
    )
