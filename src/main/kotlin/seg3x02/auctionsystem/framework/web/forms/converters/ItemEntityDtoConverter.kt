package seg3x02.auctionsystem.framework.web.forms.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.framework.jpa.entities.item.ItemJpaEntity
import seg3x02.auctionsystem.adapters.dtos.responses.ItemBrowseDto

@Mapper
interface ItemEntityDtoConverter {
    fun convertEntity(entity: ItemJpaEntity): ItemBrowseDto
}
