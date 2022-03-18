package seg3x02.auctionsystem.adapters.dtos.queries

class AccountCreateDto(
    val userName: String,
    val firstname: String,
    val lastname: String,
    val email: String
)  {
    var creditCardInfo: CreditCardCreateDto? = null
}
