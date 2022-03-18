package seg3x02.auctionsystem.application.usecases

import seg3x02.auctionsystem.adapters.dtos.queries.AccountCreateDto
import java.util.*

interface CreateAccount {
    fun createAccount(accountInfo: AccountCreateDto): Boolean
}
