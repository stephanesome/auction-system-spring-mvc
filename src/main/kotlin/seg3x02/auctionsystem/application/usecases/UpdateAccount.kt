package seg3x02.auctionsystem.application.usecases

import seg3x02.auctionsystem.adapters.dtos.AccountDto
import java.util.*

interface UpdateAccount {
    fun updateAccount(accountId: UUID, accountInfo: AccountDto): Boolean
}
