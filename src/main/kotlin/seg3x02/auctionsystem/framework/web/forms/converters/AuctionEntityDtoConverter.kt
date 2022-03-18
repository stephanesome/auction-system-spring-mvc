package seg3x02.auctionsystem.framework.web.forms.converters

import org.mapstruct.Mapping
import seg3x02.auctionsystem.framework.jpa.entities.auction.AuctionJpaEntity
import seg3x02.auctionsystem.adapters.dtos.responses.AuctionBrowseDto
import seg3x02.auctionsystem.adapters.dtos.responses.ItemBrowseDto

// @Mapper
interface AuctionEntityDtoConverter {
    @Mapping(target = "item", source = "item")
    @Mapping(target = "category", source = "entity.category.name")
    fun convertEntity(entity: AuctionJpaEntity, item: ItemBrowseDto): AuctionBrowseDto

    // fun categoryMap(category: AuctionCategoryJpaEntity): String
}
