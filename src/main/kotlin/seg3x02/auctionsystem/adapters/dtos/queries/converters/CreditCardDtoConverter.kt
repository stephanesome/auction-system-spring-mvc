package seg3x02.auctionsystem.adapters.dtos.queries.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.adapters.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard
import java.util.*

@Mapper
interface CreditCardDtoConverter {
    fun convertDto(cCardDto: CreditCardCreateDto, id: UUID): CreditCard
}
