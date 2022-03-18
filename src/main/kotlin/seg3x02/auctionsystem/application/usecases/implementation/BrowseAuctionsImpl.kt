package seg3x02.auctionsystem.application.usecases.implementation

import seg3x02.auctionsystem.adapters.dtos.responses.AuctionBrowseDto
import seg3x02.auctionsystem.application.usecases.BrowseAuctions
import seg3x02.auctionsystem.domain.auction.core.Auction
import seg3x02.auctionsystem.domain.auction.facade.AuctionFacade
import seg3x02.auctionsystem.domain.item.facade.ItemFacade
import seg3x02.auctionsystem.domain.user.core.account.UserAccount

class BrowseAuctionsImpl(private val auctionFacade: AuctionFacade,
                         private val itemFacade: ItemFacade): BrowseAuctions {
    override fun getAuctions(category: String): List<AuctionBrowseDto> {
        val auctionList = auctionFacade.getAuctionsBasedOnCategory(category)
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

    private fun getAuctionBrowseList(auctionList: List<Auction>): MutableList<AuctionBrowseDto> {
        val auctionBrowseList: MutableList<AuctionBrowseDto> = ArrayList()
        for (auc in auctionList) {
            val minBid = auctionFacade.getHighestBidAmount(auc.id)
            if (minBid != null) {
                val aucItem = itemFacade.getItem(auc.item)
                if (aucItem != null) {
                    val aucDto = AuctionBrowseDto(
                        auc.id,
                        aucItem.title,
                        aucItem.description,
                        aucItem.image,
                        auc.closeTime(),
                        minBid
                    )
                    auctionBrowseList.add(aucDto)
                }
            }
        }
        return auctionBrowseList
    }
}
