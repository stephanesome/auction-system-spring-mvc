package seg3x02.auctionsystem.infrastructure.jpa.dao

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.repository.findByIdOrNull
import seg3x02.auctionsystem.adapters.repositories.CreditCardJpaAdapter
import seg3x02.auctionsystem.domain.user.entities.creditCard.Address
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.infrastructure.jpa.entities.user.creditCard.AddressJpaEntity
import seg3x02.auctionsystem.infrastructure.jpa.entities.user.creditCard.CreditCardJpaEntity
import java.time.Month
import java.time.Year

@DataJpaTest
class CreditCardDaoTest {
    @Autowired
    lateinit var creditCardJpaAdapter: CreditCardJpaAdapter

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var creditCardRepository: CreditCardJpaRepository

    @TestConfiguration
    internal class TestConfig {
        @Bean
        fun creditCardJpaAdapter(creditCardRepository: CreditCardJpaRepository): CreditCardJpaAdapter {
            return CreditCardJpaAdapter(creditCardRepository)
        }
    }

    @Test
    fun save_creditCard() {
        val ccNumber = "5555555"
        val cc = CreditCard(ccNumber,
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

        creditCardJpaAdapter.save(cc)

        var ccFound = creditCardRepository.findByIdOrNull(ccNumber)

        Assertions.assertThat(ccFound).isNotNull
        Assertions.assertThat(ccFound?.accountFirstname).isEqualTo(cc.accountFirstname)
        Assertions.assertThat(ccFound?.accountAddress).isNotNull
        Assertions.assertThat(ccFound?.accountAddress?.postalCode).isEqualTo(cc.accountAddress.postalCode)
    }

    @Test
    fun find_creditCard() {
        val ccNumber = "5555555"
        val cc = CreditCardJpaEntity(ccNumber,
            Month.JUNE,
            Year.parse("2024"),
            "Toto",
            "Tata",
            AddressJpaEntity(
                "125 DeLa Rue",
                "Ottawa",
                "Canada",
                "K0K0K0")
        )

        entityManager.persist(cc)
        entityManager.flush()

        val ccFound =  creditCardJpaAdapter.find(ccNumber)

        Assertions.assertThat(ccFound).isNotNull
        Assertions.assertThat(ccFound?.accountFirstname).isEqualTo(cc.accountFirstname)
        Assertions.assertThat(ccFound?.accountAddress).isNotNull
        Assertions.assertThat(ccFound?.accountAddress?.postalCode).isEqualTo(cc.accountAddress.postalCode)
    }
}
