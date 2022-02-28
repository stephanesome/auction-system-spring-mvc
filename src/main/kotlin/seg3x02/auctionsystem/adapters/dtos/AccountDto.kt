package seg3x02.auctionsystem.adapters.dtos

class AccountDto(
    val firstname: String,
    val lastname: String,
    val email: String
)  {
    var creditCardInfo: CreditCardDto? = null
}
