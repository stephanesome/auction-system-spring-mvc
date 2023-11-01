package seg3x02.auctionsystem.infrastructure.web.forms

import org.springframework.web.multipart.MultipartFile
import seg3x02.auctionsystem.infrastructure.web.forms.validators.CreditCardValidator
import jakarta.validation.constraints.*

@CreditCardValidator.List(
    *[CreditCardValidator(
        numberField = "number",
        mandatoryField = "accountFirstname",
        message = "{credit-card-missing-first-name}"),
        CreditCardValidator(
            numberField = "number",
            mandatoryField = "accountLastname",
            message = "{credit-card-missing-last-name}"
        ),
        CreditCardValidator(
            numberField = "number",
            mandatoryField = "expirationMonth",
            message = "{credit-card-missing-exp-month}"
        ),
        CreditCardValidator(
            numberField = "number",
            mandatoryField = "expirationYear",
            message = "{credit-card-missing-exp-year}"
        ),
        CreditCardValidator(
            numberField = "number",
            mandatoryField = "street",
            message = "{credit-card-missing-address}"
        ),
        CreditCardValidator(
            numberField = "number",
            mandatoryField = "city",
            message = "{credit-card-missing-city}"
        ),
        CreditCardValidator(
            numberField = "number",
            mandatoryField = "country",
            message = "{credit-card-missing-country}"
        ),
        CreditCardValidator(
            numberField = "number",
            mandatoryField = "postalCode",
            message = "{credit-card-missing-postal-code}"
        )])
class AuctionForm {
    var startTime: String? = null
    @Min(1, message="{auction-create-minimum-duration}")
    var duration: Long = 0
    @DecimalMin("0.0", message="{auction-create-wrong-start-price}")
    var startPrice: Double = 0.0
    @DecimalMin("0.0", message="{auction-create-wrong-min-increment}")
    var minIncrement: Double = 0.0
    @NotEmpty(message="{auction-create-no-category}")
    var category: String = ""
    @NotEmpty(message="{auction-create-no-title}")
    var itemTitle: String = ""
    var itemDescription: String = ""
    var itemImageFile: MultipartFile? = null
    @Pattern(regexp = "^[1-9][0-9]*$|", message="{credit-card-number-wrong-format}")
    var number: String? = null
    @Min(1, message = "{credit-card-exp-month-wrong-min}")
    @Max(12, message = "{credit-card-exp-month-wrong-max}")
    var expirationMonth: Int? = null
    @Positive(message = "{credit-card-number-wrong-year}")
    var expirationYear: Int? = null
    var accountFirstname: String? = null
    var accountLastname: String? = null
    var street: String? = null
    var city: String? = null
    var country: String? = null
    var postalCode: String? = null
}
