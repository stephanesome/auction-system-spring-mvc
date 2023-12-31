package seg3x02.auctionsystem.infrastructure.configuration

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import seg3x02.auctionsystem.application.dtos.queries.BidCreateDto
import seg3x02.auctionsystem.domain.auction.entities.Auction
import seg3x02.auctionsystem.domain.auction.entities.AuctionCategory
import seg3x02.auctionsystem.domain.auction.factories.BidFactory
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.domain.auction.repositories.BidRepository
import seg3x02.auctionsystem.domain.item.entities.Item
import seg3x02.auctionsystem.domain.item.repositories.ItemRepository
import seg3x02.auctionsystem.domain.user.entities.account.PendingPayment
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.entities.creditCard.Address
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import seg3x02.auctionsystem.domain.user.repositories.AccountRepository
import seg3x02.auctionsystem.infrastructure.jpa.dao.UserJpaRepository
import seg3x02.auctionsystem.infrastructure.security.credentials.User
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.Year
import java.util.*

// @Component
class DataBaseInit(private val auctionRepository: AuctionRepository,
                   private val accountRepository: AccountRepository,
                   private val itemRepository: ItemRepository,
                   private val creditCardRepository: CreditCardRepository,
                   private val bidRepository: BidRepository,
                   private val bidFactory: BidFactory,
                   private val userRepository: UserJpaRepository,
                   private val encoder: PasswordEncoder)  : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
       setDb()
    }

    private fun setDb() {
        // create seller
        val sellerId = "seller000"
        val sellerUser = User(sellerId, encoder.encode("pass"), true)
        userRepository.save(sellerUser)
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
        false)
        val itemId = UUID.randomUUID()
        val item = Item(itemId,
            "Game Boy",
            "Very rare")
        itemRepository.save(item)
        auction.item = itemId
        seller.auctions.add(auctionId)
        // add bids to auction - ensure winner is selected
        val buyer1Id = "buyer001"
        val bid1 = auction.createBid(
            BidCreateDto(
                BigDecimal(125),
                LocalDateTime.now(),
                buyer1Id),
            bidFactory,
            bidRepository
        )
        val buyer2Id = "buyer002"
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
        false)
        val item2Id = UUID.randomUUID()

        val item2 = Item(item2Id,
            "Nintendo",
            "Very rare")
        itemRepository.save(item2)
        auction2.item = item2Id
        auctionRepository.save(auction2)
        seller.auctions.add(auction2Id)

        val auction2cId = UUID.randomUUID()
        val auction2c = Auction(auction2cId,
            LocalDateTime.now(),
            Duration.ofDays(5),
            BigDecimal(450),
            BigDecimal(20),
            seller.id,
            AuctionCategory(auctionCategory1),
            true
        )
        val item2Idc = UUID.randomUUID()

        val item2c = Item(item2Idc,
            "Nintendo-Closed",
            "Very rare")
        itemRepository.save(item2c)
        auction2c.item = item2Idc
        auctionRepository.save(auction2c)
        seller.auctions.add(auction2cId)

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
        seller.auctions.add(auction3Id)

        val user2Id = "user001"
        val user2 = User(user2Id, encoder.encode("pass"), true)
        userRepository.save(user2)
        val user2acc = UserAccount(user2Id,
            "Zaza",
            "Mala",
            "zaza@somewhere.com"
        )
        user2acc.pendingPayment = PendingPayment(BigDecimal(125.75))
        accountRepository.save(user2acc)
    }
}
