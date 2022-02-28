package seg3x02.auctionsystem.adapters.repositories

import org.mapstruct.factory.Mappers
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Primary
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import seg3x02.auctionsystem.adapters.repositories.converters.AuctionJpaConverter
import seg3x02.auctionsystem.domain.auction.core.Auction
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.framework.jpa.dao.AuctionJpaRepository
import java.util.*

@Component
@Primary
@CacheConfig(cacheNames=["auctions"])
class AuctionJpaAdapter(private val auctionRepository: AuctionJpaRepository): AuctionRepository {
    private val converter = Mappers.getMapper(AuctionJpaConverter::class.java)

    @CachePut(key = "#auction.id")
    @Transactional
    override fun save(auction: Auction): Auction {
        val auctionJpa = converter.convertToJpa(auction)
        auctionRepository.save(auctionJpa)
        return auction
    }

    @Cacheable(key = "#id")
    override fun find(id: UUID): Auction? {
        val auctionJpa = auctionRepository.findByIdOrNull(id)
        return auctionJpa?.let { converter.convertToModel(it) }
    }
}
