package seg3x02.auctionsystem.adapters.dtos.queries

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class BidCreateDto(val amount: BigDecimal,
                   val date: LocalDateTime,
                   val buyer: String) {
}
