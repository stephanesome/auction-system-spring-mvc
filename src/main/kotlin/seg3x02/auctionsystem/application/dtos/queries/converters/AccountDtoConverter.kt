package seg3x02.auctionsystem.application.dtos.queries.converters

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import seg3x02.auctionsystem.application.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount

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
