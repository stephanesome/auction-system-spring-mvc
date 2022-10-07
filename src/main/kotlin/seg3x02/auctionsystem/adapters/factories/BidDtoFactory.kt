package seg3x02.auctionsystem.adapters.factories

import org.mapstruct.factory.Mappers
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.application.dtos.queries.converters.BidDtoConverter
import seg3x02.auctionsystem.domain.auction.entities.Bid
import seg3x02.auctionsystem.domain.auction.factories.BidFactory
import java.util.*

@Primary
@Component
class BidDtoFactory: BidFactory {
    private val dtoConverter = Mappers.getMapper(BidDtoConverter::class.java)

    override fun createBid(bidInfo: BidCreateDto): Bid {
        return dtoConverter.convertDto(bidInfo, UUID.randomUUID())
    }
}
