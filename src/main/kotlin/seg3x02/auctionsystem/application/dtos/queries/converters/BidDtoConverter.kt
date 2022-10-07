package seg3x02.auctionsystem.application.dtos.queries.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.domain.auction.entities.Bid
import java.util.*

@Mapper
interface BidDtoConverter {
    fun convertDto(bidCreateDto: BidCreateDto, id: UUID): Bid
}
