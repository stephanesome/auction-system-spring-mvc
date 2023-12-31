package seg3x02.auctionsystem.application.usecases.unit

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.application.usecases.PlaceBid
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.entities.AuctionCategory
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.domain.auction.repositories.BidRepository
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
class PlaceBidImplTest {
    @Autowired
    lateinit var placeBid: PlaceBid
    @Autowired
    lateinit var auctionRepository: AuctionRepository
    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var bidRepository: BidRepository
    @Autowired
    lateinit var eventEmitter: DomainEventEmitter

    @Test
    fun placeBid_to_Auction_No_Pending_Payments() {
        // add user to user repository
        val buyerId = "buyerXXX"
        val buyer = UserAccount(buyerId,
            "Toto",
            "Tata",
            "toto@somewhere.com"
        )
        buyer.creditCardNumber = "555555555"
        buyer.pendingPayment = null
        accountRepository.save(buyer)
        // add auction to auction repository
        val auctionId = UUID.randomUUID()
        val auction = Auction(auctionId,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            "sellerXXX",
            AuctionCategory("Toy"),
            false
        )
        val itemId = UUID.randomUUID()
        val item = Item(itemId,
            "Toy",
            "Very rare")
        auction.item = itemId
        auctionRepository.save(auction)
        val bidInfo = BidCreateDto(BigDecimal(100.00), LocalDateTime.now(), buyerId)
        val result = placeBid.placeBid(buyerId, auctionId, bidInfo)
        Assertions.assertThat(result).isNotNull
        val expBidEv = (eventEmitter as EventEmitterAdapterStub).retrieveNewBidCreatedEvent()
        val newBidId = expBidEv?.bidId
        Assertions.assertThat(newBidId).isNotNull
        val newBid = newBidId?.let { bidRepository.find(it) }
        Assertions.assertThat(newBid).isNotNull
        Assertions.assertThat(newBid?.buyer).isEqualTo(buyerId)
        Assertions.assertThat(newBid?.amount).isEqualTo(BigDecimal(100.00))
    }

    @Test
    fun placeBid_to_Auction_No_Pending_Payments_multi() {
        // add user to user repository
        val buyerId = "buyerXXX"
        val buyer = UserAccount(buyerId,
            "Toto",
            "Tata",
            "toto@somewhere.com"
        )
        buyer.creditCardNumber = "555555555"
        buyer.pendingPayment = null
        accountRepository.save(buyer)
        // add auction to auction repository
        val auctionId = UUID.randomUUID()
        val auction = Auction(auctionId,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            "sellerXXX",
            AuctionCategory("Toy"),
            false
        )
        val itemId = UUID.randomUUID()
        val item = Item(itemId,
            "Toy",
            "Very rare")
        auction.item = itemId
        auctionRepository.save(auction)
        val bidInfo = BidCreateDto(BigDecimal(100.00), LocalDateTime.now(), buyerId)
        val result = placeBid.placeBid(buyerId, auctionId, bidInfo)
        Assertions.assertThat(result).isNotNull
        val expBidEv = (eventEmitter as EventEmitterAdapterStub).retrieveNewBidCreatedEvent()
        val newBidId = expBidEv?.bidId
        Assertions.assertThat(newBidId).isNotNull
        val newBid = newBidId?.let { bidRepository.find(it) }
        Assertions.assertThat(newBid).isNotNull
        Assertions.assertThat(newBid?.buyer).isEqualTo(buyerId)
        Assertions.assertThat(newBid?.amount).isEqualTo(BigDecimal(100.00))
        val bidInfo2 = BidCreateDto(BigDecimal(150.00), LocalDateTime.now(), buyerId)
        val result2 = placeBid.placeBid(buyerId, auctionId, bidInfo2)
        Assertions.assertThat(result2).isNotNull
        val bidInfo3 = BidCreateDto(BigDecimal(155.00), LocalDateTime.now(), buyerId)
        val result3 = placeBid.placeBid(buyerId, auctionId, bidInfo3)
        Assertions.assertThat(result3).isNotNull
    }

    @Test
    fun placeBid_to_Auction_Pending_Payments() {
        // add user to user repository
        val buyerId = "buyerXXX"
        val buyer = UserAccount(buyerId,
            "Toto",
            "Tata",
            "toto@somewhere.com"
        )
        buyer.creditCardNumber = "555555555"
        buyer.pendingPayment = PendingPayment(
            BigDecimal(100)
        )
        accountRepository.save(buyer)
        // add auction to auction repository
        val auctionId = UUID.randomUUID()
        val auction = Auction(auctionId,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            "sellerYYY",
            AuctionCategory("Toy"),
            false
        )
        val itemId = UUID.randomUUID()
        val item = Item(itemId,
            "Toy",
            "Very rare")
        auction.item = itemId
        auctionRepository.save(auction)
        val bidInfo = BidCreateDto(BigDecimal(150.00), LocalDateTime.now(), buyerId)
        val result = placeBid.placeBid(buyerId, auctionId, bidInfo)
        Assertions.assertThat(result).isNull()
    }
}
