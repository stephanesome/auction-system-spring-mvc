package seg3x02.auctionsystem.domain.item.factories

import seg3x02.auctionsystem.adapters.dtos.ItemDto
import seg3x02.auctionsystem.domain.item.core.Item

interface ItemFactory {
    fun createItem(itemInfo: ItemDto): Item
}
