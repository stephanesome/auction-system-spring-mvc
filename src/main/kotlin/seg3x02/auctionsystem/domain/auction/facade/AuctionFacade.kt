package seg3x02.auctionsystem.domain.auction.facade

import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.domain.auction.entities.Auction
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
    fun getActiveAuctionsBasedOnCategory(category: String): List<Auction>
    fun getMinimumBidAmount(auctionId: UUID): BigDecimal?
    fun getAuction(auctionId: UUID): Auction?
    fun getAllActiveAuctions(): List<Auction>
    fun setAuctionFee(auctionId: UUID, fee: BigDecimal)
    fun getAuctionFee(auctionId: UUID): BigDecimal?
}
