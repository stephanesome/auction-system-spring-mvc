package seg3x02.auctionsystem.application.usecases

import seg3x02.auctionsystem.adapters.dtos.AuctionDto
import java.util.*

interface CreateAuction {
    fun addAuction(auctionInfo: AuctionDto): UUID?
}
