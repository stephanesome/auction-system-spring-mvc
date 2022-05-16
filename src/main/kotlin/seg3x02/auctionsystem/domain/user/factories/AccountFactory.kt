package seg3x02.auctionsystem.domain.user.factories

import seg3x02.auctionsystem.adapters.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount

interface AccountFactory {
    fun createAccount(accountCreateDto: AccountCreateDto): UserAccount
}
