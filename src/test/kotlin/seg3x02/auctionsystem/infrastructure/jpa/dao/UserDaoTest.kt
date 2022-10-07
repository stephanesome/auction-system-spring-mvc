package seg3x02.auctionsystem.infrastructure.jpa.dao

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.repository.findByIdOrNull
import seg3x02.auctionsystem.adapters.repositories.AccountJpaAdapter
import seg3x02.auctionsystem.domain.user.entities.account.PendingPayment
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.infrastructure.jpa.entities.user.account.PendingPaymentJpaEntity
import seg3x02.auctionsystem.infrastructure.jpa.entities.user.account.UserAccountJpaEntity
import java.math.BigDecimal
import java.util.*

@DataJpaTest
class UserDaoTest {
    @Autowired
    lateinit var userJpaAdapter: AccountJpaAdapter

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var accountRepository: AccountJpaRepository

    @TestConfiguration
    internal class TestConfig {
        @Bean
        fun userJpaAdapter(accountRepository: AccountJpaRepository): AccountJpaAdapter {
            return AccountJpaAdapter(accountRepository)
        }
    }

    @Test
    fun save_user_account() {
        val userId = "userYYY"
        val user = UserAccount(userId,
            "Toto",
            "Tata",
            "toto@somewhere.com"
        )
        user.creditCardNumber = "555555555"
        user.pendingPayment = PendingPayment(
            BigDecimal(100)
        )
        user.auctions.add(UUID.randomUUID())
        user.auctions.add(UUID.randomUUID())
        user.bids.add(UUID.randomUUID())

        userJpaAdapter.save(user)

        val userFound = accountRepository.findByIdOrNull(userId)

        Assertions.assertThat(userFound).isNotNull
        Assertions.assertThat(userFound?.auctions).isNotEmpty
        Assertions.assertThat(userFound?.bids).isNotEmpty
    }

    @Test
    fun find_user_account() {
        val creditCardNumber = "555555555"
        val userId = "userYYY"
        val user = UserAccountJpaEntity(userId,
            "Toto",
            "Tata",
            "toto@somewhere.com",
        )
        user.creditCardNumber = creditCardNumber
        user.pendingPayment = PendingPaymentJpaEntity(
            BigDecimal(100)
        )
        user.auctions.add(UUID.randomUUID())
        user.auctions.add(UUID.randomUUID())
        user.bids.add(UUID.randomUUID())
        user.active = false

        entityManager.persist(user)
        entityManager.flush()

        val userFound = userJpaAdapter.find(userId)

        Assertions.assertThat(userFound).isNotNull
        Assertions.assertThat(userFound?.auctions).isNotEmpty
        Assertions.assertThat(userFound?.bids).isNotEmpty
    }
}
