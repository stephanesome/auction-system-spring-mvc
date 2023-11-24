package seg3x02.auctionsystem.adapters.repositories

import org.mapstruct.factory.Mappers
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import seg3x02.auctionsystem.adapters.repositories.converters.AuctionJpaConverter
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.infrastructure.jpa.dao.AuctionJpaRepository
import seg3x02.auctionsystem.infrastructure.jpa.entities.auction.AuctionCategoryJpaEntity
import seg3x02.auctionsystem.infrastructure.jpa.entities.auction.AuctionJpaEntity
import java.util.*
import kotlin.collections.ArrayList

@Component
@CacheConfig(cacheNames=["auctions"])
class AuctionJpaAdapter(private val auctionRepository: AuctionJpaRepository): AuctionRepository {
    private val converter = Mappers.getMapper(AuctionJpaConverter::class.java)

    @CachePut(key = "#auction.id")
    override fun save(auction: Auction): Auction {
        val auctionJpa = converter.convertToJpa(auction)
        auctionRepository.save(auctionJpa)
        return auction
    }

    @Cacheable(key = "#id")
    @Transactional
    override fun find(id: UUID): Auction? {
        val auctionJpa = auctionRepository.findByIdOrNull(id)
        return auctionJpa?.let { converter.convertToModel(it) }
    }

    @Transactional
    override fun findActiveByCategory(category: String): List<Auction> {
        val aucEnts = auctionRepository.findByCategoryAndIsclosedIsFalse(AuctionCategoryJpaEntity(category))
        return domainAuctions(aucEnts)
    }

    @Transactional
    override fun findActive(): List<Auction> {
        val aucEnts = auctionRepository.findByIsclosedFalse()
        return domainAuctions(aucEnts)
    }

    private fun domainAuctions(aucEnts: List<AuctionJpaEntity>): ArrayList<Auction> {
        val listAuctions = ArrayList<Auction>()
        for (aucEnt in aucEnts) {
            if (!aucEnt.isclosed) {
                listAuctions.add(converter.convertToModel(aucEnt))
            }
        }
        return listAuctions
    }
}
