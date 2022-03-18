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
import seg3x02.auctionsystem.framework.jpa.entities.auction.AuctionCategoryJpaEntity
import java.util.*
import kotlin.collections.ArrayList

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

    // @Cacheable
    @Transactional
    override fun findByCategory(category: String): List<Auction> {
        val aucEnts = auctionRepository.findByCategory(AuctionCategoryJpaEntity(category))
        val listAuctions = ArrayList<Auction>()
        for (aucEnt in aucEnts) {
            val auc = converter.convertToModel(aucEnt)
            listAuctions.add(auc)
        }
        return listAuctions
    }
}
