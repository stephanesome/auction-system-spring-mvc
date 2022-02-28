package seg3x02.auctionsystem.domain.auction.facade.implementation

import org.springframework.beans.factory.annotation.Autowired
import seg3x02.auctionsystem.adapters.dtos.AuctionDto
import seg3x02.auctionsystem.adapters.dtos.BidDto
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.domain.auction.events.AuctionClosed
import seg3x02.auctionsystem.domain.auction.events.NewAuctionAdded
import seg3x02.auctionsystem.domain.auction.events.NewBidCreated
import seg3x02.auctionsystem.domain.auction.facade.AuctionFacade
import seg3x02.auctionsystem.domain.auction.factories.AuctionFactory
import seg3x02.auctionsystem.domain.auction.factories.BidFactory
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.domain.auction.repositories.BidRepository
import java.time.LocalDateTime
import java.util.*

class AuctionFacadeImpl(
    private var auctionFactory: AuctionFactory,
    private var auctionRepository: AuctionRepository,
    private var eventEmitter: DomainEventEmitter): AuctionFacade {

    @Autowired
    private lateinit var bidFactory: BidFactory

    @Autowired
    private lateinit var bidRepository: BidRepository

    override fun addAuction(auctionInfo: AuctionDto, aucItemId: UUID): UUID {
        val auction = auctionFactory.createAuction(auctionInfo, aucItemId)
        auctionRepository.save(auction)
        eventEmitter.emit(NewAuctionAdded(UUID.randomUUID(), Date(), auction.id))
        return auction.id
    }

    override fun closeAuction(auctionId: UUID): UUID? {
        val auc = auctionRepository.find(auctionId)
        if (auc != null) {
            val winnerBidId = auc.close()
            auctionRepository.save(auc)
            eventEmitter.emit(
                AuctionClosed(UUID.randomUUID(),
                    Date(),
                    auc.id,
                    winnerBidId)
            )
            if (winnerBidId != null) {
                return auc.getBidSeller(winnerBidId, bidRepository)
            }
        }
        return null
    }

    override fun getAuctionSeller(auctionId: UUID): UUID? {
        return auctionRepository.find(auctionId)?.seller
    }

    override fun placeBid(auctionId: UUID, bidInfo: BidDto): UUID? {
        val auction = auctionRepository.find(auctionId)
        val bidId = auction?.createBid(bidInfo, bidFactory, bidRepository)
        return if (bidId != null) {
            auctionRepository.save(auction)
            val newBidEvent = NewBidCreated(UUID.randomUUID(),
                    Date(),
                    bidId)
            eventEmitter.emit(newBidEvent)
            bidId
        } else null
    }

    override fun includesAuctionsInProgress(auctionIds: List<UUID>): Boolean {
        for (auc in auctionIds) {
            val auction = auctionRepository.find(auc)
            if (auction != null && ! auction.isclosed) return true
        }
        return false
    }

    override fun getAuctionCloseTime(auctionId: UUID): LocalDateTime? {
        val auction = auctionRepository.find(auctionId)
        return auction?.closeTime()
    }
}
