package seg3x02.auctionsystem.application.dtos.responses

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class AuctionBrowseDto(val id: UUID,
                       val sellerId: String,
                       val itemTitle: String,
                       val itemDescription: String,
                       val itemImage: ByteArray,
                       val endTime: LocalDateTime,
                       val currentMinBid: BigDecimal,
                       var isclosed: Boolean) {
}
