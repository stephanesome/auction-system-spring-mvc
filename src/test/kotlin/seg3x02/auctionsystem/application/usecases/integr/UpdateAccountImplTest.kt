package seg3x02.auctionsystem.application.usecases.integr

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import seg3x02.auctionsystem.application.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.application.dtos.queries.AddressCreateDto
import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.application.services.CreditService
import seg3x02.auctionsystem.application.services.DomainEventEmitter
import seg3x02.auctionsystem.application.usecases.UpdateAccount
import seg3x02.auctionsystem.domain.user.entities.account.PendingPayment
import seg3x02.auctionsystem.domain.user.entities.account.UserAccount
import seg3x02.auctionsystem.domain.user.entities.creditCard.Address
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.repositories.CreditCardRepository
import seg3x02.auctionsystem.domain.user.repositories.AccountRepository
import java.math.BigDecimal
import java.time.Month
import java.time.Year

@SpringBootTest
class UpdateAccountImplTest {
    @Autowired
    lateinit var updateAccount: UpdateAccount
    @Autowired
    lateinit var creditService: CreditService
    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var creditCardRepository: CreditCardRepository
    @Autowired
    lateinit var eventEmitter: DomainEventEmitter

    @Test
    fun updateAccount_pending_payment_credit_card() {
        // create account
        val userId = "userYYY"
        val user = UserAccount(userId,
            "Toto",
            "Tata",
            "toto@somewhere.com"
        )
        val pendingAmount = BigDecimal(20)
        user.pendingPayment = PendingPayment(
            pendingAmount
        )
        val usercc = CreditCard("5555555",
            Month.JUNE,
            Year.parse("2021"),
            "Toto",
            "Tata",
            Address(
                "125 DeLa Rue",
                "Ottawa",
                "Canada",
                "K0K0K0")
        )
        creditCardRepository.save(usercc)
        user.creditCardNumber = usercc.number
        accountRepository.save(user)

        // create DTO for update
        val addr = AddressCreateDto(
            "125 DeLa Rue",
            "Ottawa",
            "Canada",
            "K0K0K0")
        val updatedCcNumber = "6666666"
        val updatedCcexYear = Year.parse("2024")
        val updatedCcexMonth = Month.AUGUST
        val cc = CreditCardCreateDto(updatedCcNumber,
            updatedCcexMonth,
            updatedCcexYear,
            "Toto",
            "Tata",
            addr
        )
        val updatedEmail = "totonew@somewhere.com"
        val userDto = AccountCreateDto(
            "user000",
            "Toto",
            "Tata",
            updatedEmail)
        userDto.creditCardInfo = cc

        updateAccount.updateAccount(userId, userDto)
        // check events and updates
        val updatedUser = accountRepository.find(userId)
        Assertions.assertThat(updatedUser).isNotNull
        Assertions.assertThat(updatedUser?.email).isEqualTo(updatedEmail)
        Assertions.assertThat(updatedUser?.pendingPayment).isNull()
        Assertions.assertThat(updatedUser?.creditCardNumber).isEqualTo(updatedCcNumber)
    }

    @Test
    fun updateAccount_no_pending_payment_credit_card() {
        // create account
        val userId = "userYYY"
        val user = UserAccount(userId,
            "Toto",
            "Tata",
            "toto@somewhere.com"
        )
        user.pendingPayment = null
        val usercc = CreditCard("5555555",
            Month.JUNE,
            Year.parse("2021"),
            "Toto",
            "Tata",
            Address(
                "125 DeLa Rue",
                "Ottawa",
                "Canada",
                "K0K0K0")
        )
        creditCardRepository.save(usercc)
        user.creditCardNumber = usercc.number
        accountRepository.save(user)

        // create DTO for update
        val addr = AddressCreateDto(
            "125 DeLa Rue",
            "Ottawa",
            "Canada",
            "K0K0K0")
        val updatedCcNumber = "6666666"
        val updatedCcexYear = Year.parse("2024")
        val updatedCcexMonth = Month.AUGUST
        val cc = CreditCardCreateDto(updatedCcNumber,
            updatedCcexMonth,
            updatedCcexYear,
            "Toto",
            "Tata",
            addr
        )
        val updatedEmail = "totonew@somewhere.com"
        val userDto = AccountCreateDto(
            "user000",
            "Toto",
            "Tata",
            updatedEmail)
        userDto.creditCardInfo = cc

        updateAccount.updateAccount(userId, userDto)
        // check events and updates
        val updatedUser = accountRepository.find(userId)
        Assertions.assertThat(updatedUser).isNotNull
        Assertions.assertThat(updatedUser?.email).isEqualTo(updatedEmail)
        Assertions.assertThat(updatedUser?.pendingPayment).isNull()
        Assertions.assertThat(updatedUser?.creditCardNumber).isEqualTo(updatedCcNumber)
    }

    @Test
    fun updateAccount_pending_payment_no_credit_card() {
        // create account
        val userId = "userYYY"
        val user = UserAccount(userId,
            "Toto",
            "Tata",
            "toto@somewhere.com"
        )
        val pendingAmount = BigDecimal(20)
        user.pendingPayment = PendingPayment(
            pendingAmount
        )
        val usercc = CreditCard("5555555",
            Month.JUNE,
            Year.parse("2021"),
            "Toto",
            "Tata",
            Address(
                "125 DeLa Rue",
                "Ottawa",
                "Canada",
                "K0K0K0")
        )
        creditCardRepository.save(usercc)
        user.creditCardNumber = usercc.number
        accountRepository.save(user)

        // create DTO for update
        val updatedEmail = "totonew@somewhere.com"
        val userDto = AccountCreateDto(
            "user000",
            "Toto",
            "Tata",
            updatedEmail)

        updateAccount.updateAccount(userId, userDto)
        // check events and updates
        val updatedUser = accountRepository.find(userId)
        Assertions.assertThat(updatedUser).isNotNull
        Assertions.assertThat(updatedUser?.email).isEqualTo(updatedEmail)
        Assertions.assertThat(updatedUser?.pendingPayment).isNotNull
    }
}

