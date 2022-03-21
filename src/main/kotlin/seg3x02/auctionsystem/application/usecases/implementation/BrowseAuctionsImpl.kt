package seg3x02.auctionsystem.application.usecases.implementation

import seg3x02.auctionsystem.adapters.dtos.responses.AuctionBrowseDto
import seg3x02.auctionsystem.application.usecases.BrowseAuctions
import seg3x02.auctionsystem.domain.auction.core.Auction
import seg3x02.auctionsystem.domain.auction.facade.AuctionFacade
import seg3x02.auctionsystem.domain.item.facade.ItemFacade
import seg3x02.auctionsystem.domain.user.core.account.UserAccount
import java.util.*
import kotlin.collections.ArrayList

class BrowseAuctionsImpl(private val auctionFacade: AuctionFacade,
                         private val itemFacade: ItemFacade): BrowseAuctions {
    override fun getAuctions(category: String): List<AuctionBrowseDto> {
        val auctionList = if (category == null || category == "")
            auctionFacade.getAllActiveAuctions()
            else auctionFacade.getActiveAuctionsBasedOnCategory(category)
        return getAuctionBrowseList(auctionList)
    }

    override fun getUserAuctions(user: UserAccount): List<AuctionBrowseDto> {
        val auctionList = ArrayList<Auction>()
        for (aucId in user.auctions) {
            val auction = auctionFacade.getAuction(aucId)
            if (auction != null) {
                auctionList.add(auction)
            }
        }
        return getAuctionBrowseList(auctionList)
    }

    override fun getAuctionBrowse(id: UUID): AuctionBrowseDto? {
        val auction = auctionFacade.getAuction(id)
        return if (auction != null) {
            auctionBrowseDto(auction)
        } else {
            null
        }
    }

    private fun getAuctionBrowseList(auctionList: List<Auction>): MutableList<AuctionBrowseDto> {
        val auctionBrowseList: MutableList<AuctionBrowseDto> = ArrayList()
        for (auc in auctionList) {
            var aucDto: AuctionBrowseDto? = auctionBrowseDto(auc)
            if (aucDto != null) {
                auctionBrowseList.add(aucDto)
            }
        }
        return auctionBrowseList
    }

    private fun auctionBrowseDto(auc: Auction): AuctionBrowseDto? {
        var aucDto: AuctionBrowseDto? = null
        val minBid = auctionFacade.getMinimumBidAmount(auc.id)
        if (minBid != null) {
            val aucItem = itemFacade.getItem(auc.item)
            if (aucItem != null) {
                aucDto = AuctionBrowseDto(
                    auc.id,
                    auc.seller,
                    aucItem.title,
                    aucItem.description,
                    aucItem.image,
                    auc.closeTime(),
                    minBid
                )
            }
        }
        return aucDto
    }
}
