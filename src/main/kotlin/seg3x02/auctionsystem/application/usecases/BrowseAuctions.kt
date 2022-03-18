package seg3x02.auctionsystem.application.usecases

import seg3x02.auctionsystem.adapters.dtos.responses.AuctionBrowseDto
import seg3x02.auctionsystem.domain.user.core.account.UserAccount

interface BrowseAuctions {
    fun getAuctions(category: String): List<AuctionBrowseDto>

    fun getUserAuctions(user: UserAccount): List<AuctionBrowseDto>
}
