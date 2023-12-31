package seg3x02.auctionsystem.application.usecases.unit

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import seg3x02.auctionsystem.application.services.EmailService
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.application.usecases.DeactivateAccount
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.entities.AuctionCategory
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.domain.user.entities.account.PendingPayment
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.repositories.AccountRepository
import seg3x02.auctionsystem.tests.config.TestBeanConfiguration
import seg3x02.auctionsystem.tests.testStubs.EventEmitterAdapterStub
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Import(TestBeanConfiguration::class)
@TestPropertySource(locations= ["classpath:application.properties"])
@SpringBootTest
internal class DeactivateAccountImplTest {
    @Autowired
    lateinit var deactivateAccount: DeactivateAccount
    @MockkBean
    lateinit var emailService: EmailService
    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var auctionRepository: AuctionRepository
    @Autowired
    lateinit var eventEmitter: DomainEventEmitter

    @Test
    fun deactivateAccount_no_pending_payment_no_auctions() {
        val userId = "sellerXXX"
        val userEmail = "toto@somewhere.com"
        val user = UserAccount(userId,
            "Toto",
            "Tata",
            userEmail
        )
        accountRepository.save(user)
        val origUser = accountRepository.find(userId)
        Assertions.assertThat(origUser?.active).isTrue

        every {emailService.sendAccountDeactivationEmail(userEmail) } just Runs

        deactivateAccount.deactivateAccount(userId)
        val deacUser = accountRepository.find(userId)
        Assertions.assertThat(deacUser?.active).isFalse

        val expDeac = (eventEmitter as EventEmitterAdapterStub).retrieveUserAccountDeactivatedEvent()
        Assertions.assertThat(expDeac).isNotNull

        verify {emailService.sendAccountDeactivationEmail(userEmail)}
    }

    @Test
    fun deactivateAccount_pending_payment_no_auctions() {
        val userId = "sellerXXX"
        val user = UserAccount(userId,
            "Toto",
            "Tata",
            "toto@somewhere.com"
        )
        user.pendingPayment = PendingPayment(
            BigDecimal(120)
        )
        accountRepository.save(user)
        val origUser = accountRepository.find(userId)
        Assertions.assertThat(origUser?.active).isTrue

        deactivateAccount.deactivateAccount(userId)
        val deacUser = accountRepository.find(userId)
        Assertions.assertThat(deacUser?.active).isTrue
    }

    @Test
    fun deactivateAccount_no_pending_payment_closed_auction() {
        // create user account
        val userId = "sellerXXX"
        val userEmail = "toto@somewhere.com"
        val user = UserAccount(userId,
            "Toto",
            "Tata",
            userEmail
        )
        user.creditCardNumber = "5555555555"

        // create auctions added to user account
        val auction1Id = UUID.randomUUID()
        val auction1 = Auction(auction1Id,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            user.id,
            AuctionCategory("Toy"),
            false
        )
        val item1Id = UUID.randomUUID()
        val item1 = Item(item1Id,
            "Toy",
            "Very rare")
        auction1.item = item1Id
        auction1.isclosed = true
        user.auctions.add(auction1Id)
        auctionRepository.save(auction1)
        accountRepository.save(user)

        every {emailService.sendAccountDeactivationEmail(userEmail) } just Runs

        Assertions.assertThat(user?.active).isTrue
        deactivateAccount.deactivateAccount(userId)
        val deacUser = accountRepository.find(userId)
        Assertions.assertThat(deacUser?.active).isFalse

        verify {emailService.sendAccountDeactivationEmail(userEmail)}
    }

    @Test
    fun deactivateAccount_no_pending_payment_active_auction() {
        // create user account
        val userId = "sellerXXX"
        val userEmail = "toto@somewhere.com"
        val user = UserAccount(userId,
            "Toto",
            "Tata",
            userEmail
        )
        user.creditCardNumber = "5555555555"

        // create auctions added to user account
        val auction1Id = UUID.randomUUID()
        val auction1 = Auction(auction1Id,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            user.id,
            AuctionCategory("Toy"),
            false
        )
        val item1Id = UUID.randomUUID()
        val item1 = Item(item1Id,
            "Toy",
            "Very rare")
        auction1.item = item1Id
        auction1.isclosed = false
        user.auctions.add(auction1Id)
        auctionRepository.save(auction1)
        accountRepository.save(user)

        Assertions.assertThat(user?.active).isTrue
        deactivateAccount.deactivateAccount(userId)
        val deacUser = accountRepository.find(userId)
        Assertions.assertThat(deacUser?.active).isTrue
    }

    @Test
    fun deactivateAccount_no_pending_payment_active_and_closed_auction() {
        // create user account
        val userId = "sellerXXX"
        val userEmail = "toto@somewhere.com"
        val user = UserAccount(userId,
            "Toto",
            "Tata",
            userEmail
        )
        user.creditCardNumber = "5555555555"

        // create auctions added to user account
        val auction1Id = UUID.randomUUID()
        val auction1 = Auction(auction1Id,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            user.id,
            AuctionCategory("Toy"),
            false
        )
        val item1Id = UUID.randomUUID()
        val item1 = Item(item1Id,
            "Toy",
            "Very rare")
        auction1.item = item1Id
        auction1.isclosed = true
        user.auctions.add(auction1Id)
        auctionRepository.save(auction1)

        val auction2Id = UUID.randomUUID()
        val auction2 = Auction(auction2Id,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            user.id,
            AuctionCategory("Toy"),
            false
        )
        val item2Id = UUID.randomUUID()
        val item2 = Item(item2Id,
            "Toy",
            "Very rare")
        auction2.item = item2Id
        auction2.isclosed = false
        user.auctions.add(auction2Id)
        auctionRepository.save(auction2)

        val auction3Id = UUID.randomUUID()
        val auction3 = Auction(auction3Id,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            user.id,
            AuctionCategory("Toy"),
            false
        )
        val item3Id = UUID.randomUUID()
        val item3 = Item(item3Id,
            "Toy",
            "Very rare")
        auction3.item = item3Id
        auction3.isclosed = true
        user.auctions.add(auction3Id)
        auctionRepository.save(auction3)

        accountRepository.save(user)

        Assertions.assertThat(user?.active).isTrue
        deactivateAccount.deactivateAccount(userId)
        val deacUser = accountRepository.find(userId)
        Assertions.assertThat(deacUser?.active).isTrue
    }
}
