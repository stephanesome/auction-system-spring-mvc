package seg3x02.auctionsystem.tests.fixtures

import seg3x02.auctionsystem.domain.user.core.account.UserAccount
import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.repositories.UserRepository
import java.util.*

class UserRepositoryStub : UserRepository {
    private val users: MutableMap<UUID, UserAccount> = HashMap()

    override fun save(user: UserAccount): UserAccount {
        users[user.id] = user
        return user
    }

    override fun find(userId: UUID): UserAccount? = users[userId]
}
