package seg3x02.auctionsystem.adapters.repositories.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.domain.auction.entities.Bid
import seg3x02.auctionsystem.framework.jpa.entities.auction.BidJpaEntity

@Mapper
interface BidJpaConverter {
    fun convertToJpa(bid: Bid): BidJpaEntity

    fun convertToModel(bidJpa: BidJpaEntity): Bid
}
