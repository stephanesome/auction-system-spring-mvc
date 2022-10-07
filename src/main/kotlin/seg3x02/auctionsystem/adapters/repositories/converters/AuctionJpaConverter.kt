package seg3x02.auctionsystem.adapters.repositories.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.entities.AuctionCategory
import seg3x02.auctionsystem.infrastructure.jpa.entities.auction.AuctionCategoryJpaEntity
import seg3x02.auctionsystem.infrastructure.jpa.entities.auction.AuctionJpaEntity

@Mapper
interface AuctionJpaConverter {
    fun convertToJpa(auction: Auction): AuctionJpaEntity
    fun convertCategoryToJpa(auctionCateg: AuctionCategory): AuctionCategoryJpaEntity
    fun convertToModel(actionJpa: AuctionJpaEntity): Auction
    fun convertCategoryToModel(auctionJpaCateg: AuctionCategoryJpaEntity): AuctionCategory
}
