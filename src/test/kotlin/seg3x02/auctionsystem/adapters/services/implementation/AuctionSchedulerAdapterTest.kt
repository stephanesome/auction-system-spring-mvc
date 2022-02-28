package seg3x02.auctionsystem.adapters.services.implementation

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import seg3x02.auctionsystem.adapters.dtos.BidDto
import seg3x02.auctionsystem.application.services.AuctionScheduler
import seg3x02.auctionsystem.domain.auction.core.Auction
import seg3x02.auctionsystem.domain.auction.core.AuctionCategory
import seg3x02.auctionsystem.domain.auction.factories.BidFactory
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.domain.auction.repositories.BidRepository
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
class AuctionSchedulerAdapterTest {
    @Autowired
    lateinit var auctionScheduler: AuctionScheduler

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var auctionRepository: AuctionRepository

    @Autowired
    lateinit var creditCardRepository: CreditCardRepository

    @Autowired
    private lateinit var bidRepository: BidRepository

    @Autowired
    private lateinit var bidFactory: BidFactory

    @Test
    fun scheduleClose() {
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
        val auctionId = UUID.randomUUID()
        val auction = Auction(auctionId,
            LocalDateTime.now(),
            Duration.ofMinutes(1),
            BigDecimal(100),
            BigDecimal(5),
            seller.id,
            AuctionCategory("Toy")
        )
        // add bids to auction - ensure winner is selected
        val buyer1Id = UUID.randomUUID()
        val bid1 = auction.createBid(
            BidDto(BigDecimal(125),
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
            BidDto(BigDecimal(150),
                LocalDateTime.now(),
                buyer2Id),
            bidFactory,
            bidRepository
        )
        auctionRepository.save(auction)

        val closeTime = LocalDateTime.now().plusSeconds(10)
        Assertions.assertThat(auction.isclosed).isFalse
        val schedule = auctionScheduler.scheduleClose(auctionId, closeTime)
        Thread.sleep(10000)
        val aucFound =  auctionRepository.find(auctionId)
        Assertions.assertThat(aucFound).isNotNull
        Assertions.assertThat(aucFound?.isclosed).isTrue
    }
}
