package seg3x02.auctionsystem.domain.item.facade

import seg3x02.auctionsystem.adapters.dtos.ItemDto
import java.util.*

interface ItemFacade {
    fun addItem(itemInfo: ItemDto): UUID
}
