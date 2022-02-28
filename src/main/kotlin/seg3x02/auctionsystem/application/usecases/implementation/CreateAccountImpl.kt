package seg3x02.auctionsystem.application.usecases.implementation

import seg3x02.auctionsystem.adapters.dtos.AccountDto
import seg3x02.auctionsystem.application.usecases.CreateAccount
import seg3x02.auctionsystem.domain.user.facade.UserFacade
import java.util.*

class CreateAccountImpl(private var userFacade: UserFacade): CreateAccount {
    override fun createAccount(accountInfo: AccountDto): UUID? {
        return userFacade.createAccount(accountInfo)
    }
}
