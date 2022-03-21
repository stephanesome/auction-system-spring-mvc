package seg3x02.auctionsystem.framework.web.forms.converters

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import seg3x02.auctionsystem.adapters.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.adapters.dtos.queries.AddressCreateDto
import seg3x02.auctionsystem.adapters.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.adapters.dtos.responses.AccountViewDto
import seg3x02.auctionsystem.framework.web.forms.AccountForm
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

    fun mapNumberLong(value: Number): Long {
        return value.toLong()
    }

    fun mapIntMonth(value: Int): Month {
        return Month.of(value)
    }

    fun mayIntYear(value: Int): Year {
        return Year.of(value)
    }

    fun mapMonthInt(value: Month): Int {
        return value.value
    }

    fun mayYearInt(value: Year): Int {
        return value.value
    }
}
