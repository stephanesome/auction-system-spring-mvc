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
import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.services.EmailService
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.application.usecases.CloseAuction
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.entities.AuctionCategory
import seg3x02.auctionsystem.domain.auction.factories.BidFactory
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.domain.auction.repositories.BidRepository
import seg3x02.auctionsystem.domain.auction.services.AuctionFeeCalculator
import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.entities.creditCard.Address
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import seg3x02.auctionsystem.domain.user.repositories.AccountRepository
import seg3x02.auctionsystem.tests.config.TestBeanConfiguration
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.Year
import java.util.*

@Import(TestBeanConfiguration::class)
@TestPropertySource(locations= ["classpath:application.properties"])
@SpringBootTest
class CloseAuctionImplTest {
    @Autowired
    lateinit var closeAuction: CloseAuction
    @MockkBean
    lateinit var creditService: CreditService
    @MockkBean
    lateinit var emailService: EmailService
    @Autowired
    lateinit var auctionRepository: AuctionRepository
    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var creditCardRepository: CreditCardRepository
    @Autowired
    private lateinit var bidRepository: BidRepository
    @Autowired
    lateinit var eventEmitter: DomainEventEmitter
    @Autowired
    private lateinit var bidFactory: BidFactory

    @Test
    fun closeAuction_seller_has_no_pending_payment_processed_right() {
        // create seller
        val sellerId = "sellerId"
        val seller = UserAccount(sellerId,
            "Toto",
            "Tata",
            "toto@somewhere.com"
        )
        val sellercc = CreditCard("5555555",
            Month.JUNE,
            Year.parse("2024"),
            "Toto",
            "Tata",
            Address(
                "125 DeLa Rue",
                "Ottawa",
                "Canada",
                "K0K0K0")
        )
        creditCardRepository.save(sellercc)
        seller.creditCardNumber = sellercc.number
        accountRepository.save(seller)
        // create auction
        val auctionId = UUID.randomUUID()
        val auction = Auction(auctionId,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            seller.id,
            AuctionCategory("Toy"),
            false
        )
        auction.fee = BigDecimal(0)
        val itemId = UUID.randomUUID()
        Item(itemId,
            "Toy",
            "Very rare")
        auction.item = itemId
        // add bids to auction - ensure winner is selected
        val buyer1Id = "buyer1Id"
        val bid1 = auction.createBid(
            BidCreateDto(BigDecimal(125),
            LocalDateTime.now(),
            buyer1Id),
            bidFactory,
            bidRepository
        )
        val buyer2Id = "buyer2Id"
        val buyer2 = UserAccount(buyer2Id,
            "Roro",
            "Zaza",
            "roro@somewhere.com"
        )
        accountRepository.save(buyer2)
        val bid2 = auction.createBid(
            BidCreateDto(BigDecimal(150),
                LocalDateTime.now(),
                buyer2Id),
            bidFactory,
            bidRepository
        )
        auctionRepository.save(auction)
        // set credit to be successful
        every {creditService.processPayment(sellercc.number,
            sellercc.expirationMonth,
            sellercc.expirationYear,
            BigDecimal(0))} returns true

        every {emailService.sendSellerCompletedAuctionPaymentProcessedFine(seller.email)} just Runs
        every {emailService.sendWinnerCompletedAuctionEmailWithWinningBid(buyer2.email)} just Runs

        // call service
        val winnerId = closeAuction.closeAuction(auction.id)
        // assert that expected bid is the winner
        Assertions.assertThat(winnerId).isNotNull
        Assertions.assertThat(winnerId).isEqualTo(buyer2Id)
        // verify that email service was invoked
        verify {emailService.sendSellerCompletedAuctionPaymentProcessedFine(seller.email)}
        verify {emailService.sendWinnerCompletedAuctionEmailWithWinningBid(buyer2.email)}
    }

    @Test
    fun closeAuction_seller_has_no_pending_payment_processed_fail() {
        // create seller
        val sellerId = "sellerId"
        val seller = UserAccount(sellerId,
            "Toto",
            "Tata",
            "toto@somewhere.com"
        )
        val sellercc = CreditCard("5555555",
            Month.JUNE,
            Year.parse("2024"),
            "Toto",
            "Tata",
            Address(
                "125 DeLa Rue",
                "Ottawa",
                "Canada",
                "K0K0K0")
        )
        creditCardRepository.save(sellercc)
        seller.creditCardNumber = sellercc.number
        accountRepository.save(seller)
        // create auction
        val auctionId = UUID.randomUUID()
        val auction = Auction(auctionId,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            seller.id,
            AuctionCategory("Toy"),
            false
        )
        auction.fee = BigDecimal(0)
        // add bids to auction - ensure winner is selected
        val buyer1Id = "buyer1Id"
        val bid1 = auction.createBid(
            BidCreateDto(BigDecimal(125),
                LocalDateTime.now(),
                buyer1Id),
            bidFactory,
            bidRepository
        )
        val buyer2Id = "buyer2Id"
        val buyer2 = UserAccount(buyer2Id,
            "Roro",
            "Zaza",
            "roro@somewhere.com"
        )
        accountRepository.save(buyer2)
        val bid2 = auction.createBid(
            BidCreateDto(BigDecimal(150),
                LocalDateTime.now(),
                buyer2Id),
            bidFactory,
            bidRepository
        )
        val itemId = UUID.randomUUID()
        Item(itemId,
            "Toy",
            "Very rare")
        auction.item = itemId
        auctionRepository.save(auction)
        // set credit to be successful
        every {creditService.processPayment(sellercc.number,
            sellercc.expirationMonth,
            sellercc.expirationYear,
            BigDecimal(0))} returns false

        every {emailService.sendSellerCompletedAuctionPaymentProcessFail(seller.email)} just Runs
        every {emailService.sendWinnerCompletedAuctionEmailWithWinningBid(buyer2.email)} just Runs

        // call service
        val winnerId = closeAuction.closeAuction(auction.id)
        // assert that expected bid is the winner
        Assertions.assertThat(winnerId).isNotNull
        Assertions.assertThat(winnerId).isEqualTo(buyer2Id)
        // verify that email service was invoked
        verify {emailService.sendSellerCompletedAuctionPaymentProcessFail(seller.email)}
        verify {emailService.sendWinnerCompletedAuctionEmailWithWinningBid(buyer2.email)}
    }

    @Test
    fun closeAuction_no_bids() {
        // create seller
        val sellerId = "sellerId"
        val seller = UserAccount(sellerId,
            "Toto",
            "Tata",
            "toto@somewhere.com"
        )
        val sellercc = CreditCard("5555555",
            Month.JUNE,
            Year.parse("2024"),
            "Toto",
            "Tata",
            Address(
                "125 DeLa Rue",
                "Ottawa",
                "Canada",
                "K0K0K0")
        )
        creditCardRepository.save(sellercc)
        seller.creditCardNumber = sellercc.number
        accountRepository.save(seller)
        // create auction
        val auctionId = UUID.randomUUID()
        val auction = Auction(auctionId,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            seller.id,
            AuctionCategory("Toy"),
            false
        )
        val itemId = UUID.randomUUID()
        Item(itemId,
            "Toy",
            "Very rare")
        auction.item = itemId
        auctionRepository.save(auction)

        every {emailService.sendSellerCompletedAuctionEmailNoWinningBid(seller.email)} just Runs

        // call service
        val winnerId = closeAuction.closeAuction(auction.id)
        // assert that expected bid is the winner
        Assertions.assertThat(winnerId).isNull()
        // verify that email service was invoked
        verify {emailService.sendSellerCompletedAuctionEmailNoWinningBid(seller.email)}
    }
}
