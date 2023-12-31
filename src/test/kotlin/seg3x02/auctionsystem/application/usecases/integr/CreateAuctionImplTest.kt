package seg3x02.auctionsystem.application.usecases.integr

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import seg3x02.auctionsystem.application.dtos.queries.AddressCreateDto
import seg3x02.auctionsystem.application.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.application.dtos.queries.ItemCreateDto
import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.application.usecases.CreateAuction
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.domain.user.entities.account.PendingPayment
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.repositories.AccountRepository
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.Year

@SpringBootTest
class CreateAuctionImplTest {
    @Autowired
    lateinit var createAuction: CreateAuction
    @Autowired
    lateinit var creditService: CreditService
    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var eventEmitter: DomainEventEmitter
    @Autowired
    lateinit var auctionRepository: AuctionRepository

    @Test
    fun addAuction_user_with_credit_card_no_pending() {
        val sellerId = "sellerXXX"

        val seller = UserAccount(sellerId,
            "Toto",
            "Tata",
            "toto@somewhere.com")
        seller.creditCardNumber = "555555555"

        accountRepository.save(seller)

        val itemInfo = ItemCreateDto("Game boy",
            "Still wrapped in")

        val aucDto = AuctionCreateDto(
            LocalDateTime.now(),
            Duration.ofDays(5),
            BigDecimal(100.00),
            BigDecimal(5.00),
            sellerId,
            "Toy",
            itemInfo)



        val aucId = createAuction.addAuction(aucDto)
        Assertions.assertThat(aucId).isNotNull
        val newAuction = aucId?.let { auctionRepository.find(it) }
        Assertions.assertThat(newAuction?.seller).isEqualTo(sellerId)
    }

    @Test
    fun addAuction_user_with_pending_credit_card_info_provided() {
        val sellerId = "sellerXXX"

        val seller = UserAccount(sellerId,
            "Toto",
            "Tata",
            "toto@somewhere.com")
        seller.creditCardNumber = null
        val pendingAmount = BigDecimal(100)
        seller.pendingPayment = PendingPayment(
            pendingAmount
        )
        accountRepository.save(seller)

        val itemInfo = ItemCreateDto("Game boy",
            "Still wrapped in")

        val aucDto = AuctionCreateDto(
            LocalDateTime.now(),
            Duration.ofDays(5),
            BigDecimal(100.00),
            BigDecimal(5.00),
            sellerId,
            "Toy",
            itemInfo)
        val addr = AddressCreateDto(
            "125 DeLa Rue",
            "Ottawa",
            "Canada",
            "K0K0K0")
        val ccNumber = "6666666"
        val ccexYear = Year.parse("2024")
        val ccexMonth = Month.AUGUST
        val ccInfo = CreditCardCreateDto(ccNumber,
            ccexMonth,
            ccexYear,
            "Toto",
            "Tata",
            addr
        )
        aucDto.creditCardInfo = ccInfo


        val aucId = createAuction.addAuction(aucDto)
        Assertions.assertThat(aucId).isNotNull
        val auction = aucId?.let { auctionRepository.find(it) }
        Assertions.assertThat(auction).isNotNull
        val user = accountRepository.find(sellerId)
        Assertions.assertThat(user).isNotNull
        Assertions.assertThat(user?.pendingPayment).isNull()
    }

    @Test
    fun addAuction_user_with_pending_no_credit_card_info_provided() {
        val sellerId = "sellerXXX"

        val seller = UserAccount(sellerId,
            "Toto",
            "Tata",
            "toto@somewhere.com")
        seller.creditCardNumber = null
        val pendingAmount = BigDecimal(100)
        seller.pendingPayment = PendingPayment(
            pendingAmount
        )
        accountRepository.save(seller)

        val itemInfo = ItemCreateDto("Game boy",
            "Still wrapped in")

        val aucDto = AuctionCreateDto(
            LocalDateTime.now(),
            Duration.ofDays(5),
            BigDecimal(100.00),
            BigDecimal(5.00),
            sellerId,
            "Toy",
            itemInfo)


        val aucId = createAuction.addAuction(aucDto)
        Assertions.assertThat(aucId).isNull()

    }

    @Test
    fun addAuction_user_with_no_pending_no_credit_card_info_provided() {
        val sellerId = "sellerXXX"

        val seller = UserAccount(sellerId,
            "Toto",
            "Tata",
            "toto@somewhere.com")
        seller.creditCardNumber = null
        seller.pendingPayment = null
        accountRepository.save(seller)

        val itemInfo = ItemCreateDto("Game boy",
            "Still wrapped in")

        val aucDto = AuctionCreateDto(
            LocalDateTime.now(),
            Duration.ofDays(5),
            BigDecimal(100.00),
            BigDecimal(5.00),
            sellerId,
            "Toy",
            itemInfo)


        val aucId = createAuction.addAuction(aucDto)
        Assertions.assertThat(aucId).isNull()

    }
}
