package seg3x02.auctionsystem.infrastructure.jpa.entities.auction

import jakarta.persistence.Embeddable

@Embeddable
data class AuctionCategoryJpaEntity(var name: String)
