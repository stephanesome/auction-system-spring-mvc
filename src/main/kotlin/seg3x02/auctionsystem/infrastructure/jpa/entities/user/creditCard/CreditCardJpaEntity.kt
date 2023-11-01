package seg3x02.auctionsystem.infrastructure.jpa.entities.user.creditCard

import java.time.Month
import java.time.Year
import jakarta.persistence.*

@Entity()
@Table(name="CREDIT_CARD")
class CreditCardJpaEntity(
    @Id
    val number: String,
    val expirationMonth: Month,
    val expirationYear: Year,
    val accountFirstname: String,
    val accountLastname: String,
    @Embedded
    val accountAddress: AddressJpaEntity,
) {

}
