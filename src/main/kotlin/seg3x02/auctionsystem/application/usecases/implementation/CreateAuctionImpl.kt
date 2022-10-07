package seg3x02.auctionsystem.application.usecases.implementation

import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.application.services.AuctionScheduler
import seg3x02.auctionsystem.application.usecases.CreateAuction
import seg3x02.auctionsystem.domain.auction.facade.AuctionFacade
import seg3x02.auctionsystem.domain.auction.services.AuctionFeeCalculator
import seg3x02.auctionsystem.domain.item.facade.ItemFacade
import seg3x02.auctionsystem.domain.user.facade.UserFacade
import java.math.BigDecimal
import java.util.*

class CreateAuctionImpl(
    private var userFacade: UserFacade,
    private var itemFacade: ItemFacade,
    private var auctionFacade: AuctionFacade,
    private var auctionScheduler: AuctionScheduler,
    private var auctionFeeCalculator: AuctionFeeCalculator): CreateAuction {

    override fun addAuction(auctionInfo: AuctionCreateDto): UUID? {
        val ccInfo = auctionInfo.creditCardInfo
        if (ccInfo != null) {
            userFacade.addCreditCard(auctionInfo.seller, ccInfo)
        }
        val pendingP = userFacade.hasPendingPayment(auctionInfo.seller)
        val creditCard = userFacade.getCreditCardNumber(auctionInfo.seller)
        if (!pendingP && (creditCard != null) ) {
            val aucItemId = itemFacade.addItem(auctionInfo.itemInfo)
            val auctionId = auctionFacade.addAuction(auctionInfo, aucItemId)
            val fee = auctionFeeCalculator.getAuctionFee(auctionId)
            auctionFacade.setAuctionFee(auctionId, fee)
            userFacade.addAuctionToSeller(auctionInfo.seller, auctionId)
            val auctionCloseTime = auctionFacade.getAuctionCloseTime(auctionId)
            if (auctionCloseTime != null) {
                auctionScheduler.scheduleClose(auctionId, auctionCloseTime)
            }
            return auctionId
        }
        return null
    }
}
