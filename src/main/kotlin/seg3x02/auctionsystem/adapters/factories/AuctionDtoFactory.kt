package seg3x02.auctionsystem.adapters.factories

import org.mapstruct.factory.Mappers
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.application.dtos.queries.converters.AuctionDtoConverter
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.factories.AuctionFactory
import java.util.*

@Primary
@Component
class AuctionDtoFactory: AuctionFactory {
    private val dtoConverter = Mappers.getMapper(AuctionDtoConverter::class.java)

    override fun createAuction(
        auctionInfo: AuctionCreateDto,
        aucItemId: UUID
    ): Auction {
        val auction = dtoConverter.convertDto(auctionInfo, UUID.randomUUID())
        auction.item = aucItemId
        return auction
    }
}
