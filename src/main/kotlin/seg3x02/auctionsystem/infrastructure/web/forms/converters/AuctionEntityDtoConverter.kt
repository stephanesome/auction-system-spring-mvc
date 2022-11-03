package seg3x02.auctionsystem.infrastructure.web.forms.converters

import org.mapstruct.Mapping
import seg3x02.auctionsystem.infrastructure.jpa.entities.auction.AuctionJpaEntity
import seg3x02.auctionsystem.application.dtos.responses.AuctionBrowseDto
import seg3x02.auctionsystem.application.dtos.responses.ItemBrowseDto

interface AuctionEntityDtoConverter {
    @Mapping(target = "item", source = "item")
    @Mapping(target = "category", source = "entity.category.name")
    fun convertEntity(entity: AuctionJpaEntity, item: ItemBrowseDto): AuctionBrowseDto
}
