package seg3x02.auctionsystem.application.usecases.implementation

import seg3x02.auctionsystem.adapters.dtos.BidDto
import seg3x02.auctionsystem.application.usecases.PlaceBid
import seg3x02.auctionsystem.domain.auction.facade.AuctionFacade
import seg3x02.auctionsystem.domain.user.facade.UserFacade
import java.util.*

class PlaceBidImpl(
    private var userFacade: UserFacade,
    private var auctionFacade: AuctionFacade): PlaceBid {

    override fun placeBid(accountId: UUID, auctionId: UUID, bidInfo: BidDto): UUID? {
        val pendingPayment = userFacade.getPendingPayment(accountId)
        if (pendingPayment == null) {
            val bidId = auctionFacade.placeBid(auctionId, bidInfo)
            if (bidId != null) {
                userFacade.addBidToAccount(accountId, bidId)

                return bidId
            }
        }
        return null
    }
}
