package seg3x02.auctionsystem.application.usecases.integr

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.application.usecases.BrowseAuctions
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.entities.AuctionCategory
import seg3x02.auctionsystem.domain.auction.factories.BidFactory
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.domain.auction.repositories.BidRepository
import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.domain.item.repositories.ItemRepository
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.entities.creditCard.Address
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import seg3x02.auctionsystem.domain.user.repositories.AccountRepository
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.Year
import java.util.*

@SpringBootTest
internal class BrowseAuctionsImplTest {
    @Autowired
    lateinit var browseAuctions: BrowseAuctions
    @Autowired
    lateinit var auctionRepository: AuctionRepository
    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var itemRepository: ItemRepository
    @Autowired
    lateinit var creditCardRepository: CreditCardRepository
    @Autowired
    private lateinit var bidRepository: BidRepository
    @Autowired
    private lateinit var bidFactory: BidFactory

    @Test
    fun getAuctions() {
        // create seller
        val sellerId = "seller001"
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
        val auctionCategory1 = "Toy"
        val auctionId = UUID.randomUUID()
        val auction = Auction(auctionId,
            LocalDateTime.now(),
            Duration.ofDays(3),
            BigDecimal(100),
            BigDecimal(5),
            seller.id,
            AuctionCategory(auctionCategory1),
            false
        )
        val itemId = UUID.randomUUID()
        val item = Item(itemId,
            "Game Boy",
            "Very rare")
        itemRepository.save(item)
        auction.item = itemId
        // add bids to auction - ensure winner is selected
        val buyer1Id = "buyer1Id"
        val bid1 = auction.createBid(
            BidCreateDto(
                BigDecimal(125),
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
            BidCreateDto(
                BigDecimal(150),
                LocalDateTime.now(),
                buyer2Id),
            bidFactory,
            bidRepository
        )
        auctionRepository.save(auction)

        val auction2Id = UUID.randomUUID()
        val auction2 = Auction(auction2Id,
            LocalDateTime.now(),
            Duration.ofDays(10),
            BigDecimal(250),
            BigDecimal(2),
            seller.id,
            AuctionCategory(auctionCategory1),
            false
        )
        val item2Id = UUID.randomUUID()

        val item2 = Item(item2Id,
            "Nintendo",
            "Very rare")
        itemRepository.save(item2)
        auction2.item = item2Id
        auctionRepository.save(auction2)

        val auctionCategory2 = "Book"
        val auction3Id = UUID.randomUUID()
        val auction3 = Auction(auction3Id,
            LocalDateTime.now(),
            Duration.ofMinutes(30),
            BigDecimal(20),
            BigDecimal(10),
            seller.id,
            AuctionCategory(auctionCategory2),
            false
        )
        val item3Id = UUID.randomUUID()

        val item3 = Item(item3Id,
            "Spring Book",
            "Excellent condition")
        itemRepository.save(item3)
        auction3.item = item3Id
        auction3.createBid(
            BidCreateDto(
                BigDecimal(25),
                LocalDateTime.now(),
                buyer1Id),
            bidFactory,
            bidRepository
        )
        auction3.createBid(
            BidCreateDto(
                BigDecimal(45),
                LocalDateTime.now(),
                buyer2Id),
            bidFactory,
            bidRepository
        )
        auctionRepository.save(auction3)

        val listAuctions = browseAuctions.getAuctions(auctionCategory1)
        Assertions.assertThat(listAuctions).isNotNull
        Assertions.assertThat(listAuctions.size).isEqualTo(2)
        val listAuctions2 = browseAuctions.getAuctions(auctionCategory2)
        Assertions.assertThat(listAuctions2.size).isEqualTo(1)
        val auctionBr = listAuctions2[0]
        Assertions.assertThat(auctionBr).isNotNull
        Assertions.assertThat(auctionBr.itemTitle).isEqualTo(item3.title)
        val listAuctions3 = browseAuctions.getAuctions("Hehe")
        Assertions.assertThat(listAuctions3).isNotNull
        Assertions.assertThat(listAuctions3.size).isEqualTo(0)
    }
}
