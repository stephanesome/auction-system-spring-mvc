package seg3x02.auctionsystem.tests.fixtures

import seg3x02.auctionsystem.domain.auction.core.Auction
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import java.util.*
import kotlin.collections.HashMap

class AuctionRepositoryStub : AuctionRepository {
    private val auctions: MutableMap<UUID, Auction> = HashMap()

    override fun save(auction: Auction): Auction {
        auctions[auction.id] = auction
        return auction
    }

    override fun find(id: UUID): Auction? = auctions[id]

    override fun findByCategory(category: String): List<Auction> {
        TODO("Not yet implemented")
    }
}
