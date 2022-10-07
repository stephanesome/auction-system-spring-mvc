package seg3x02.auctionsystem.application.usecases

import seg3x02.auctionsystem.application.dtos.responses.AuctionBrowseDto
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import java.util.*

interface BrowseAuctions {
    fun getAuctions(category: String): List<AuctionBrowseDto>

    fun getUserAuctions(user: UserAccount): List<AuctionBrowseDto>

    fun getAuctionBrowse(id: UUID): AuctionBrowseDto?
}
