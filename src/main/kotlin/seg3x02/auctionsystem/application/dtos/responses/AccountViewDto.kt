package seg3x02.auctionsystem.application.dtos.responses

import java.time.Month
import java.time.Year

data class AccountViewDto(
    val userName: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val pendingPayment: Double,
    val auctions: MutableList<AuctionBrowseDto>,
    var creditCardNumber: String? = null,
    var expirationMonth: Month? = null,
    var expirationYear: Year? = null,
    var accountFirstname: String? = null,
    var accountLastname: String? = null,
    var street: String? = null,
    var city: String? = null,
    var country: String? = null,
    var postalCode: String? = null) {
}
