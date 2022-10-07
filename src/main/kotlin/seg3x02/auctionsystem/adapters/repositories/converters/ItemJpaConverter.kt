package seg3x02.auctionsystem.adapters.repositories.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.infrastructure.jpa.entities.item.ItemJpaEntity

@Mapper
interface ItemJpaConverter {
    fun convertToJpa(item: Item): ItemJpaEntity

    fun convertToModel(itemJpa: ItemJpaEntity): Item
}
