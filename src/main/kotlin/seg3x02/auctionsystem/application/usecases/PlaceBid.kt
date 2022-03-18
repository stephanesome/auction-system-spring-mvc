package seg3x02.auctionsystem.application.usecases

import seg3x02.auctionsystem.adapters.dtos.queries.BidCreateDto
import java.util.*

interface PlaceBid {
    fun placeBid(accountId: String, auctionId: UUID, bidInfo: BidCreateDto): UUID?
}
