package seg3x02.auctionsystem.domain.auction.facade

import seg3x02.auctionsystem.adapters.dtos.AuctionDto
import seg3x02.auctionsystem.adapters.dtos.BidDto
import seg3x02.auctionsystem.domain.auction.core.Auction
import java.time.LocalDateTime
import java.util.*

interface AuctionFacade {
    fun addAuction(auctionInfo: AuctionDto, aucItemId: UUID): UUID
    fun closeAuction(auctionId: UUID): UUID?
    fun getAuctionSeller(auctionId: UUID): UUID?
    fun placeBid(auctionId: UUID, bidInfo: BidDto): UUID?
    fun includesAuctionsInProgress(auctionIds: List<UUID>): Boolean
    fun getAuctionCloseTime(auctionId: UUID): LocalDateTime?
}
