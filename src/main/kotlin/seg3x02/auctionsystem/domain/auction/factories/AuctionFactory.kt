package seg3x02.auctionsystem.domain.auction.factories

import seg3x02.auctionsystem.adapters.dtos.AuctionDto
import seg3x02.auctionsystem.domain.auction.core.Auction
import java.util.*

interface AuctionFactory {
    fun createAuction(auctionInfo: AuctionDto, aucItemId: UUID): Auction
}
