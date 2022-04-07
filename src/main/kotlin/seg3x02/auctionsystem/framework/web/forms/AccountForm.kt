package seg3x02.auctionsystem.framework.web.forms

import seg3x02.auctionsystem.framework.web.forms.validators.CreditCardValidator
import seg3x02.auctionsystem.framework.web.forms.validators.PasswordMatch
import javax.validation.constraints.*

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
@PasswordMatch(
    passwordField = "password",
    passwordConfField = "passwordConf",
    message="{account-password-not-match}"
)
class AccountForm {
    @Size(min=2, message = "{account-create-username-short}")
    var userName: String? = null
    @NotEmpty(message ="{account-create-no-password}")
    var password: String? = null
    var passwordConf: String? = null
    var firstname: String? = null
    var lastname: String? = null
    @NotEmpty(message ="{account-create-no-email}")
    // @Pattern(regexp = "^(.+)@(\\S+)$", message="{account-create-wrong-email-format}")
    @Email(message="{account-create-wrong-email-format}")
    var email: String? = null
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
