package seg3x02.auctionsystem.adapters.dtos

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class BidDto(val amount: BigDecimal,
             val date: LocalDateTime,
             val buyer: UUID) {
}
