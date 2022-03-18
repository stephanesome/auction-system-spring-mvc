package seg3x02.auctionsystem.framework.web.forms.converters

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import seg3x02.auctionsystem.adapters.dtos.queries.AddressCreateDto
import seg3x02.auctionsystem.adapters.dtos.queries.AuctionCreateDto
import seg3x02.auctionsystem.adapters.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.adapters.dtos.queries.ItemCreateDto
import seg3x02.auctionsystem.framework.web.forms.AccountForm
import seg3x02.auctionsystem.framework.web.forms.AuctionForm
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.Year

@Mapper
abstract class AuctionFormDtoConverter {
    @Mappings(
        Mapping(target = "creditCardInfo", ignore = true),
        Mapping(target = "seller", source = "userId")
    )
    abstract fun convertAuction(userId: String, formData: AuctionForm): AuctionCreateDto

    @Mapping(target = "accountAddress", source = "address")
    abstract fun convertCreditCard(formData: AuctionForm, address: AddressCreateDto): CreditCardCreateDto

    abstract fun convertAddress(formData: AuctionForm): AddressCreateDto

    @Mappings(
        Mapping(target = "image", ignore = true),
        Mapping(target = "title", source = "itemTitle"),
        Mapping(target = "description", source = "itemDescription")
    )
    abstract fun convertItem(formData: AuctionForm): ItemCreateDto

    fun mapDuration(value: Long): Duration {
        return Duration.ofDays(value)
    }

    fun mapStartTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }

    fun mapDouble(value: Double): BigDecimal {
        return BigDecimal(value)
    }

    fun mapMonth(value: Int): Month {
        return Month.of(value)
    }

    fun mayYear(value: Int): Year {
        return Year.of(value)
    }
}
