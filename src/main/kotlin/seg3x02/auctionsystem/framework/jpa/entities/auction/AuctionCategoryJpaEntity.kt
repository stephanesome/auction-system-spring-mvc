package seg3x02.auctionsystem.framework.jpa.entities.auction

import javax.persistence.Embeddable

@Embeddable
data class AuctionCategoryJpaEntity(var name: String)
