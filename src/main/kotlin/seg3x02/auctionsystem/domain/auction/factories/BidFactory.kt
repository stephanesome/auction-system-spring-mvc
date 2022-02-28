package seg3x02.auctionsystem.domain.auction.factories

import seg3x02.auctionsystem.adapters.dtos.BidDto
import seg3x02.auctionsystem.domain.auction.core.Bid
import java.util.*

interface BidFactory {
    fun createBid(bidInfo: BidDto): Bid
}
