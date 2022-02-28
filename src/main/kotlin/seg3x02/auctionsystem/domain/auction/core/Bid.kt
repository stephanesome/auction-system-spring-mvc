package seg3x02.auctionsystem.domain.auction.core

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class Bid(val id: UUID,
          val amount: BigDecimal,
          val date: LocalDateTime,
          val buyer: UUID)
