package seg3x02.auctionsystem.domain.item.factories

import seg3x02.auctionsystem.adapters.dtos.queries.ItemCreateDto
import seg3x02.auctionsystem.domain.item.core.Item

interface ItemFactory {
    fun createItem(itemInfo: ItemCreateDto): Item
}
