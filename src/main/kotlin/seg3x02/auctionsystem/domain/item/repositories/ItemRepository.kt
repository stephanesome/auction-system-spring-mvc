package seg3x02.auctionsystem.domain.item.repositories

import seg3x02.auctionsystem.domain.item.core.Item
import java.util.*

interface ItemRepository {
    fun find(id: UUID): Item?

    fun save(item: Item): Item
}
