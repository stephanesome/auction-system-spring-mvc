package seg3x02.auctionsystem.domain.auction.facade

import seg3x02.auctionsystem.adapters.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.adapters.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.domain.auction.core.Auction
import seg3x02.auctionsystem.domain.auction.core.Bid
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

interface AuctionFacade {
    fun addAuction(auctionInfo: AuctionCreateDto, aucItemId: UUID): UUID
    fun closeAuction(auctionId: UUID): String?
    fun getAuctionSeller(auctionId: UUID): String?
    fun placeBid(auctionId: UUID, bidInfo: BidCreateDto): UUID?
    fun includesAuctionsInProgress(auctionIds: List<UUID>): Boolean
    fun getAuctionCloseTime(auctionId: UUID): LocalDateTime?
    fun getAuctionsBasedOnCategory(category: String): List<Auction>
    fun getHighestBidAmount(auctionId: UUID): BigDecimal?
    fun getAuction(auctionId: UUID): Auction?
}
