package seg3x02.auctionsystem.domain.user.factories

import seg3x02.auctionsystem.adapters.dtos.AccountDto
import seg3x02.auctionsystem.domain.user.core.account.UserAccount

interface AccountFactory {
    fun createAccount(accountDto: AccountDto): UserAccount
}
