package seg3x02.auctionsystem.infrastructure.jpa.dao

import org.springframework.data.repository.CrudRepository
import seg3x02.auctionsystem.infrastructure.jpa.entities.auction.BidJpaEntity
import java.util.*

interface BidJpaRepository: CrudRepository<BidJpaEntity, UUID> {
}
