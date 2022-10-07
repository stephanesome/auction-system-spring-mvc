package seg3x02.auctionsystem.infrastructure.jpa.entities.auction

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity()
@Table(name="BIDS")
data class BidJpaEntity(@Id val id: UUID,
                        val amount: BigDecimal,
                        val date: LocalDateTime,
                        val buyer: String
    )
