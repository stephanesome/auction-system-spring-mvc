package seg3x02.auctionsystem.adapters.dtos

import java.time.Month
import java.time.Year

data class CreditCardDto(
    val number: Number,
    val expirationMonth: Month,
    val expirationYear: Year,
    val accountFirstname: String,
    val accountLastname: String,
    val accountAddress: AddressDto
)  {

}
