package seg3x02.auctionsystem.domain.auction.repositories

import seg3x02.auctionsystem.domain.auction.core.Bid
import java.util.*

interface BidRepository {
    fun find(id: UUID): Bid?
    fun save(bid: Bid): Bid
}
