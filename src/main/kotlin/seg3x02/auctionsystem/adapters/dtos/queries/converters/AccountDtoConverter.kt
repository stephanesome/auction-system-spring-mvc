package seg3x02.auctionsystem.adapters.dtos.queries.converters

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import seg3x02.auctionsystem.adapters.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.domain.user.core.account.UserAccount
import java.util.*

@Mapper
interface AccountDtoConverter {
    @Mappings(
        Mapping(target = "creditCardNumber", ignore = true ),
        Mapping(target = "auctions", ignore = true),
        Mapping(target = "bids", ignore = true),
        Mapping(target = "active", ignore = true),
        Mapping(target = "pendingPayment", ignore = true),
        Mapping(target = "id", source = "userName")
    )
    fun convertDto(accountCreateDto: AccountCreateDto): UserAccount

}
