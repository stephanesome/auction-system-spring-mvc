package seg3x02.auctionsystem.adapters.repositories.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.domain.auction.core.Auction
import seg3x02.auctionsystem.domain.auction.core.AuctionCategory
import seg3x02.auctionsystem.framework.jpa.entities.auction.AuctionCategoryJpaEntity
import seg3x02.auctionsystem.framework.jpa.entities.auction.AuctionJpaEntity

@Mapper
interface AuctionJpaConverter {
    fun convertToJpa(auction: Auction): AuctionJpaEntity
    fun convertCategoryToJpa(auctionCateg: AuctionCategory): AuctionCategoryJpaEntity
    fun convertToModel(actionJpa: AuctionJpaEntity): Auction
    fun convertCategoryToModel(auctionJpaCateg: AuctionCategoryJpaEntity): AuctionCategory
}
