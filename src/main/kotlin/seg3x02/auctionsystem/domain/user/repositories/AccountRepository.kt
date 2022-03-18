package seg3x02.auctionsystem.domain.user.repositories

import seg3x02.auctionsystem.domain.user.core.account.UserAccount
import java.util.*

interface AccountRepository {
    fun find(userId: String): UserAccount?
    fun save(account: UserAccount): UserAccount
}
