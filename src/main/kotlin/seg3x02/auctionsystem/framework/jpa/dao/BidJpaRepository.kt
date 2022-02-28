package seg3x02.auctionsystem.framework.jpa.dao

import org.springframework.data.repository.CrudRepository
import seg3x02.auctionsystem.framework.jpa.entities.auction.BidJpaEntity
import java.util.*

interface BidJpaRepository: CrudRepository<BidJpaEntity, UUID> {
}
