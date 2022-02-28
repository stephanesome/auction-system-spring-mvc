package seg3x02.auctionsystem.adapters.dtos.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.adapters.dtos.ItemDto
import seg3x02.auctionsystem.domain.item.core.Item
import java.util.*

@Mapper
interface ItemDtoConverter {
    fun convertDto(itemDto: ItemDto, id: UUID): Item
}
