package seg3x02.auctionsystem.adapters.dtos.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.adapters.dtos.BidDto
import seg3x02.auctionsystem.domain.auction.core.Bid
import java.util.*

@Mapper
interface BidDtoConverter {
    fun convertDto(bidDto: BidDto, id: UUID): Bid
}
