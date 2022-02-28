package seg3x02.auctionsystem.adapters.dtos.converters

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import seg3x02.auctionsystem.adapters.dtos.AuctionDto
import seg3x02.auctionsystem.domain.auction.core.Auction
import seg3x02.auctionsystem.domain.auction.core.AuctionCategory
import java.util.*

@Mapper
interface AuctionDtoConverter {
    @Mappings(
        Mapping(target = "isclosed", ignore = true ),
        Mapping(target = "bids", ignore = true ),
        Mapping(target = "item", ignore = true)
    )
    fun convertDto(aucDto: AuctionDto, id: UUID): Auction

    @Mapping(target = "name", source = "category" )
    fun categoryMap(category: String): AuctionCategory
}
