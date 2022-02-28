package seg3x02.auctionsystem.application.usecases.integr

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import seg3x02.auctionsystem.adapters.dtos.BidDto
import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.application.services.EmailService
import seg3x02.auctionsystem.application.usecases.CloseAuction
import seg3x02.auctionsystem.domain.auction.core.Auction
import seg3x02.auctionsystem.domain.auction.core.AuctionCategory
import seg3x02.auctionsystem.domain.auction.factories.BidFactory
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.domain.auction.repositories.BidRepository
import seg3x02.auctionsystem.domain.item.core.Item
import seg3x02.auctionsystem.domain.user.core.account.UserAccount
import seg3x02.auctionsystem.domain.user.core.creditCard.Address
import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import seg3x02.auctionsystem.domain.user.repositories.UserRepository
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.Year
import java.util.*

@SpringBootTest
class CloseAuctionImplTest {
    @Autowired
    lateinit var closeAuction: CloseAuction
    @Autowired
    lateinit var creditService: CreditService
    @Autowired
    lateinit var emailService: EmailService
    @Autowired
    lateinit var auctionRepository: AuctionRepository
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var creditCardRepository: CreditCardRepository
    @Autowired
    private lateinit var bidRepository: BidRepository

    @Autowired
    private lateinit var bidFactory: BidFactory

    @Test
    fun closeAuction_seller_has_no_pending_payment_processed_right() {
        // create seller
        val sellerId = UUID.randomUUID()
        val seller = UserAccount(sellerId,
            "Toto",
            "Tata",
            "toto@somewhere.com"
        )
        val sellercc = CreditCard(5555555,
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
        userRepository.save(seller)
        // create auction
        val auctionId = UUID.randomUUID()
        val auction = Auction(auctionId,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            seller.id,
            AuctionCategory("Toy")
        )
        val itemId = UUID.randomUUID()
        Item(itemId,
                "Toy",
                "Very rare")
        auction.item = itemId
        // add bids to auction - ensure winner is selected
        val buyer1Id = UUID.randomUUID()
        val bid1 = auction.createBid(
            BidDto(
                BigDecimal(125),
                LocalDateTime.now(),
                buyer1Id),
            bidFactory,
            bidRepository
        )
        val buyer2Id = UUID.randomUUID()
        val buyer2 = UserAccount(buyer2Id,
            "Roro",
            "Zaza",
            "roro@somewhere.com"
        )
        userRepository.save(buyer2)
        val bid2 = auction.createBid(
            BidDto(
                BigDecimal(150),
                LocalDateTime.now(),
                buyer2Id),
            bidFactory,
            bidRepository
        )
        auctionRepository.save(auction)

        // call service
        val winnerId = closeAuction.closeAuction(auction.id)
        // assert that expected bid is the winner
        Assertions.assertThat(winnerId).isNotNull
        Assertions.assertThat(winnerId).isEqualTo(buyer2Id)
    }

    @Test
    fun closeAuction_no_bids() {
        // create seller
        val sellerId = UUID.randomUUID()
        val seller = UserAccount(sellerId,
            "Toto",
            "Tata",
            "toto@somewhere.com"
        )
        val sellercc = CreditCard(5555555,
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
        userRepository.save(seller)
        // create auction
        val auctionId = UUID.randomUUID()
        val auction = Auction(auctionId,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            seller.id,
            AuctionCategory("Toy")
        )
        val itemId = UUID.randomUUID()
        Item(itemId,
            "Toy",
            "Very rare")
        auction.item = itemId
        auctionRepository.save(auction)

        // call service
        val winnerId = closeAuction.closeAuction(auction.id)
        // assert that expected bid is the winner
        Assertions.assertThat(winnerId).isNull()
    }
}
