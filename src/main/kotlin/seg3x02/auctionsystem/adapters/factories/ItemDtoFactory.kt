package seg3x02.auctionsystem.adapters.factories

import org.mapstruct.factory.Mappers
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import seg3x02.auctionsystem.application.dtos.queries.ItemCreateDto
import seg3x02.auctionsystem.application.dtos.queries.converters.ItemDtoConverter
import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.domain.item.factories.ItemFactory
import java.util.*

@Primary
@Component
class ItemDtoFactory: ItemFactory {
    private val dtoConverter = Mappers.getMapper(ItemDtoConverter::class.java)

    override fun createItem(itemInfo: ItemCreateDto): Item {
        return dtoConverter.convertDto(itemInfo, UUID.randomUUID())
    }
}
