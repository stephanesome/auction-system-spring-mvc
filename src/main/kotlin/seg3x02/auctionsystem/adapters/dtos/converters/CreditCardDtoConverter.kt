package seg3x02.auctionsystem.adapters.dtos.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.adapters.dtos.CreditCardDto
import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard
import java.util.*

@Mapper
interface CreditCardDtoConverter {
    fun convertDto(cCardDto: CreditCardDto, id: UUID): CreditCard
}
