package seg3x02.auctionsystem.adapters.dtos.queries.converters

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import seg3x02.auctionsystem.adapters.dtos.responses.AccountViewDto
import seg3x02.auctionsystem.adapters.dtos.responses.AuctionBrowseDto
import seg3x02.auctionsystem.domain.user.core.account.PendingPayment
import seg3x02.auctionsystem.domain.user.core.account.UserAccount
import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard

@Mapper
abstract class AccountViewConverter {

    @Mappings(
        Mapping(target = "auctions", source = "aucList"),
        Mapping(target = "userName", source = "account.id")
    )
    abstract fun convertToView(account: UserAccount, aucList: List<AuctionBrowseDto>): AccountViewDto

    @Mappings(
        Mapping(target = "auctions", source = "aucList"),
        Mapping(target = "userName", source = "account.id"),
        Mapping(target = ".", source = "creditCard"),
        Mapping(target = ".", source = "creditCard.accountAddress")
    )
    abstract fun convertToViewCCard(account: UserAccount, aucList: List<AuctionBrowseDto>, creditCard: CreditCard): AccountViewDto

    fun mapPendingPayment(pendingPayment: PendingPayment?): Double {
        return pendingPayment?.amount?.toDouble() ?: 0.0
    }
}
