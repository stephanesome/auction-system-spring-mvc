package seg3x02.auctionsystem.application.usecases

import seg3x02.auctionsystem.adapters.dtos.BidDto
import java.util.*

interface PlaceBid {
    fun placeBid(accountId: UUID, auctionId: UUID, bidInfo: BidDto): UUID?
}
