package seg3x02.auctionsystem.adapters.factories

import org.mapstruct.factory.Mappers
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import seg3x02.auctionsystem.adapters.dtos.BidDto
import seg3x02.auctionsystem.adapters.dtos.converters.BidDtoConverter
import seg3x02.auctionsystem.domain.auction.core.Bid
import seg3x02.auctionsystem.domain.auction.factories.BidFactory
import java.util.*

@Primary
@Component
class BidDtoFactory: BidFactory {
    private val dtoConverter = Mappers.getMapper(BidDtoConverter::class.java)

    override fun createBid(bidInfo: BidDto): Bid {
        return dtoConverter.convertDto(bidInfo, UUID.randomUUID())
    }
}
