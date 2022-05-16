package seg3x02.auctionsystem.adapters.repositories

import org.mapstruct.factory.Mappers
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import seg3x02.auctionsystem.adapters.repositories.converters.BidJpaConverter
import seg3x02.auctionsystem.domain.auction.entities.Bid
import seg3x02.auctionsystem.domain.auction.repositories.BidRepository
import seg3x02.auctionsystem.framework.jpa.dao.BidJpaRepository
import java.util.*

@Component
@CacheConfig(cacheNames=["bids"])
class BidJpaAdapter(private val bidRepository: BidJpaRepository): BidRepository {
    private val converter = Mappers.getMapper(BidJpaConverter::class.java)

    @Cacheable(key = "#id")
    override fun find(id: UUID): Bid? {
        val bidJpa = bidRepository.findByIdOrNull(id)
        return bidJpa?.let { converter.convertToModel(it) }
    }

    @CachePut(key = "#bid.id")
    override fun save(bid: Bid): Bid {
        val bidJpa = converter.convertToJpa(bid)
        bidRepository.save(bidJpa)
        return bid
    }
}
