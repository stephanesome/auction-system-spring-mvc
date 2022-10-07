package seg3x02.auctionsystem.infrastructure.web.forms.converters

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import seg3x02.auctionsystem.application.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.application.dtos.queries.AddressCreateDto
import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.application.dtos.responses.AccountViewDto
import seg3x02.auctionsystem.infrastructure.web.forms.AccountForm
import java.time.Month
import java.time.Year

@Mapper
abstract class AccountFormDtoConverter {
    @Mapping(target = "creditCardInfo", ignore = true)
    abstract fun convertFormAccount(formData: AccountForm): AccountCreateDto

    @Mapping(target = "accountAddress", source = "address")
    abstract fun convertFormCreditCard(formData: AccountForm, address: AddressCreateDto): CreditCardCreateDto

    abstract fun convertFormAddress(formData: AccountForm): AddressCreateDto

    @Mappings(
        Mapping(target = "number", source = "creditCardNumber")
    )
    abstract fun convertDtoAccountView(account: AccountViewDto): AccountForm

    fun mapNumberLong(value: Number?): Long? {
        return value?.toLong()
    }

    fun mapIntMonth(value: Int?): Month? {
        return value?.let { Month.of(it) }
    }

    fun mayIntYear(value: Int?): Year? {
        return value?.let { Year.of(it) }
    }

    fun mapMonthInt(value: Month?): Int? {
        return value?.value
    }

    fun mayYearInt(value: Year?): Int? {
        return value?.value
    }
}
