package seg3x02.auctionsystem.adapters.dtos.queries

import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

class AuctionCreateDto(val startTime: LocalDateTime,
                       val duration: Duration,
                       val startPrice: BigDecimal,
                       val minIncrement: BigDecimal,
                       val seller: String,
                       val category: String) {
    lateinit var itemInfo: ItemCreateDto
    var creditCardInfo: CreditCardCreateDto? = null
}
