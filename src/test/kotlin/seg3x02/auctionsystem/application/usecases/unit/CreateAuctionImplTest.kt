package seg3x02.auctionsystem.application.usecases.unit

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import seg3x02.auctionsystem.adapters.dtos.AddressDto
import seg3x02.auctionsystem.adapters.dtos.AuctionDto
import seg3x02.auctionsystem.adapters.dtos.CreditCardDto
import seg3x02.auctionsystem.adapters.dtos.ItemDto
import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.application.usecases.CreateAuction
import seg3x02.auctionsystem.domain.auction.events.NewAuctionAdded
import seg3x02.auctionsystem.domain.auction.repositories.AuctionRepository
import seg3x02.auctionsystem.domain.user.core.account.PendingPayment
import seg3x02.auctionsystem.domain.user.core.account.UserAccount
import seg3x02.auctionsystem.domain.user.repositories.UserRepository
import seg3x02.auctionsystem.tests.config.TestBeanConfiguration
import seg3x02.auctionsystem.tests.fixtures.EventEmitterAdapterStub
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.Year
import java.util.*

@Import(TestBeanConfiguration::class)
@TestPropertySource(locations= ["classpath:application.properties"])
@SpringBootTest
class CreateAuctionImplTest {
        @Autowired
        lateinit var createAuction: CreateAuction
        @MockkBean
        lateinit var creditService: CreditService
        @Autowired
        lateinit var userRepository: UserRepository
        @Autowired
        lateinit var eventEmitter: DomainEventEmitter
        @Autowired
        lateinit var auctionRepository: AuctionRepository

        @Test
        fun addAuction_user_with_credit_card_no_pending() {
                val sellerId = UUID.randomUUID()

                val seller = UserAccount(sellerId,
                        "Toto",
                        "Tata",
                        "toto@somewhere.com")
                seller.creditCardNumber = 555555555

                userRepository.save(seller)

                val aucDto = AuctionDto(
                        LocalDateTime.now(),
                        Duration.ofDays(5),
                        BigDecimal(100.00),
                        BigDecimal(5.00),
                        sellerId,
                        "Toy")

                aucDto.itemInfo = ItemDto("Game boy",
                "Still wrapped in")

                createAuction.addAuction(aucDto)

                val expAuc = (eventEmitter as EventEmitterAdapterStub).retrieveNewAuctionAddedEvent()

                Assertions.assertThat(expAuc).isNotNull
                val aucId = (expAuc as NewAuctionAdded).auctionId
                val newAuction = auctionRepository.find(aucId)
                Assertions.assertThat(newAuction?.seller).isEqualTo(sellerId)
        }

        @Test
        fun addAuction_user_with_pending_credit_card_info_provided() {
                val sellerId = UUID.randomUUID()

                val seller = UserAccount(sellerId,
                        "Toto",
                        "Tata",
                        "toto@somewhere.com")
                seller.creditCardNumber = null
                val pendingAmount = BigDecimal(100)
                seller.pendingPayment = PendingPayment(
                        pendingAmount
                )
                userRepository.save(seller)

                val aucDto = AuctionDto(LocalDateTime.now(),
                        Duration.ofDays(5),
                        BigDecimal(100.00),
                        BigDecimal(5.00),
                        sellerId,
                        "Toy")
                val addr = AddressDto(
                        "125 DeLa Rue",
                        "Ottawa",
                        "Canada",
                        "K0K0K0")
                val ccNumber = 6666666
                val ccexYear = Year.parse("2024")
                val ccexMonth = Month.AUGUST
                val ccInfo = CreditCardDto(ccNumber,
                        ccexMonth,
                        ccexYear,
                        "Toto",
                        "Tata",
                        addr
                )
                aucDto.creditCardInfo = ccInfo
                aucDto.itemInfo = ItemDto("Game boy",
                        "Still wrapped in")

                every {creditService.processPayment(ccNumber,
                        ccexMonth,
                        ccexYear,
                        pendingAmount)} returns true

                val aucId = createAuction.addAuction(aucDto)
                Assertions.assertThat(aucId).isNotNull
                val auction = aucId?.let { auctionRepository.find(it) }
                Assertions.assertThat(auction).isNotNull
                val user = userRepository.find(sellerId)
                Assertions.assertThat(user).isNotNull
                Assertions.assertThat(user?.pendingPayment).isNull()

                verify {creditService.processPayment(ccNumber,
                        ccexMonth,
                        ccexYear,
                        pendingAmount)}

        }

        @Test
        fun addAuction_user_with_pending_no_credit_card_info_provided() {
                val sellerId = UUID.randomUUID()

                val seller = UserAccount(sellerId,
                        "Toto",
                        "Tata",
                        "toto@somewhere.com")
                seller.creditCardNumber = null
                val pendingAmount = BigDecimal(100)
                seller.pendingPayment = PendingPayment(
                        pendingAmount
                )
                userRepository.save(seller)

                val aucDto = AuctionDto(LocalDateTime.now(),
                        Duration.ofDays(5),
                        BigDecimal(100.00),
                        BigDecimal(5.00),
                        sellerId,
                        "Toy")
                aucDto.itemInfo = ItemDto("Game boy",
                        "Still wrapped in")

                val aucId = createAuction.addAuction(aucDto)
                Assertions.assertThat(aucId).isNull()

        }

        @Test
        fun addAuction_user_with_no_pending_no_credit_card_info_provided() {
                val sellerId = UUID.randomUUID()

                val seller = UserAccount(sellerId,
                        "Toto",
                        "Tata",
                        "toto@somewhere.com")
                seller.creditCardNumber = null
                seller.pendingPayment = null
                userRepository.save(seller)

                val aucDto = AuctionDto(LocalDateTime.now(),
                        Duration.ofDays(5),
                        BigDecimal(100.00),
                        BigDecimal(5.00),
                        sellerId,
                        "Toy")
                aucDto.itemInfo = ItemDto("Game boy",
                        "Still wrapped in")

                val aucId = createAuction.addAuction(aucDto)
                Assertions.assertThat(aucId).isNull()

        }

        @Test
        fun addAuction_user_with_pending_credit_card_info_provided_failed_payment() {
                val sellerId = UUID.randomUUID()

                val seller = UserAccount(sellerId,
                        "Toto",
                        "Tata",
                        "toto@somewhere.com")
                seller.creditCardNumber = null
                val pendingAmount = BigDecimal(100)
                seller.pendingPayment = PendingPayment(
                        pendingAmount
                )
                userRepository.save(seller)

                val aucDto = AuctionDto(LocalDateTime.now(),
                        Duration.ofDays(5),
                        BigDecimal(100.00),
                        BigDecimal(5.00),
                        sellerId,
                        "Toy")
                val addr = AddressDto(
                        "125 DeLa Rue",
                        "Ottawa",
                        "Canada",
                        "K0K0K0")
                val ccNumber = 6666666
                val ccexYear = Year.parse("2024")
                val ccexMonth = Month.AUGUST
                val ccInfo = CreditCardDto(ccNumber,
                        ccexMonth,
                        ccexYear,
                        "Toto",
                        "Tata",
                        addr
                )
                aucDto.creditCardInfo = ccInfo
                aucDto.itemInfo = ItemDto("Game boy",
                        "Still wrapped in")

                every {creditService.processPayment(ccNumber,
                        ccexMonth,
                        ccexYear,
                        pendingAmount)} returns false

                val aucId = createAuction.addAuction(aucDto)
                Assertions.assertThat(aucId).isNull()

                verify {creditService.processPayment(ccNumber,
                        ccexMonth,
                        ccexYear,
                        pendingAmount)}

        }
    }
