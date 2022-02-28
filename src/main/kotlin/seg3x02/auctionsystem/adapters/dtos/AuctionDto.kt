package seg3x02.auctionsystem.adapters.dtos

import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

class AuctionDto(val startTime: LocalDateTime,
                 val duration: Duration,
                 val startPrice: BigDecimal,
                 val minIncrement: BigDecimal,
                 val seller: UUID,
                 val category: String) {
    lateinit var itemInfo: ItemDto
    var creditCardInfo: CreditCardDto? = null
}
