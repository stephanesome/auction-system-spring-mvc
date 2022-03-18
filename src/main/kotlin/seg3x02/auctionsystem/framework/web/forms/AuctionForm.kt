package seg3x02.auctionsystem.framework.web.forms

import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime

class AuctionForm {
    var startTime: String? = null
    var duration: Long = 0
    var startPrice: Double = 0.0
    var minIncrement: Double = 0.0
    // var seller: String = ""
    var category: String = ""
    var itemTitle: String = ""
    var itemDescription: String = ""
    var number: Long? = null
    var expirationMonth: Int? = null
    var expirationYear: Int? = null
    var accountFirstname: String? = null
    var accountLastname: String? = null
    var street: String? = null
    var city: String? = null
    var country: String? = null
    var postalCode: String? = null
    var itemImageFile: MultipartFile? = null
}
