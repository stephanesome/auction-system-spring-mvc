package seg3x02.auctionsystem.application.dtos.queries.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.application.dtos.queries.ItemCreateDto
import seg3x02.auctionsystem.domain.item.entities.Item
import java.util.*

@Mapper
interface ItemDtoConverter {
    fun convertDto(itemCreateDto: ItemCreateDto, id: UUID): Item
}
