package seg3x02.auctionsystem.infrastructure.jpa.dao

import org.springframework.data.repository.CrudRepository
import seg3x02.auctionsystem.infrastructure.jpa.entities.auction.AuctionCategoryJpaEntity
import seg3x02.auctionsystem.infrastructure.jpa.entities.auction.AuctionJpaEntity
import java.util.*

interface AuctionJpaRepository: CrudRepository<AuctionJpaEntity, UUID> {
    fun findByCategory(category: AuctionCategoryJpaEntity): List<AuctionJpaEntity>
    fun findByCategoryAndIsclosedIsFalse(category: AuctionCategoryJpaEntity): List<AuctionJpaEntity>
    fun findByIsclosedFalse(): List<AuctionJpaEntity>
}
