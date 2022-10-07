package seg3x02.auctionsystem.infrastructure.jpa.dao

import org.springframework.data.repository.CrudRepository
import seg3x02.auctionsystem.infrastructure.jpa.entities.item.ItemJpaEntity
import java.util.*

interface ItemJpaRepository: CrudRepository<ItemJpaEntity, UUID> {
}
