package seg3x02.auctionsystem.adapters.factories

import org.mapstruct.factory.Mappers
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import seg3x02.auctionsystem.adapters.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.adapters.dtos.queries.converters.AccountDtoConverter
import seg3x02.auctionsystem.domain.user.core.account.UserAccount
import seg3x02.auctionsystem.domain.user.factories.AccountFactory
import java.util.*

@Primary
@Component
class AccountDtoFactory: AccountFactory {
    private val dtoConverter = Mappers.getMapper(AccountDtoConverter::class.java)

    override fun createAccount(accountCreateDto: AccountCreateDto): UserAccount {
        return dtoConverter.convertDto(accountCreateDto)
    }
}
