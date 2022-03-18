package seg3x02.auctionsystem.framework.web.forms.converters

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import seg3x02.auctionsystem.adapters.dtos.queries.AccountCreateDto
import seg3x02.auctionsystem.adapters.dtos.queries.AddressCreateDto
import seg3x02.auctionsystem.adapters.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.framework.web.forms.AccountForm
import java.time.Month
import java.time.Year
import javax.persistence.criteria.CriteriaBuilder

@Mapper
abstract class AccountFormDtoConverter {
    @Mapping(target = "creditCardInfo", ignore = true)
    abstract fun convertAccount(formData: AccountForm): AccountCreateDto

    @Mapping(target = "accountAddress", source = "address")
    abstract fun convertCreditCard(formData: AccountForm, address: AddressCreateDto): CreditCardCreateDto

    abstract fun convertAddress(formData: AccountForm): AddressCreateDto

    fun mapMonth(value: Int): Month {
        return Month.of(value)
    }

    fun mayYear(value: Int): Year {
        return Year.of(value)
    }
}
