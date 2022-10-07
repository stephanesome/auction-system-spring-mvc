package seg3x02.auctionsystem.infrastructure.web.forms.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.infrastructure.jpa.entities.item.ItemJpaEntity
import seg3x02.auctionsystem.application.dtos.responses.ItemBrowseDto

@Mapper
interface ItemEntityDtoConverter {
    fun convertEntity(entity: ItemJpaEntity): ItemBrowseDto
}
