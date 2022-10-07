package seg3x02.auctionsystem.application.dtos.queries.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.application.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import java.util.*

@Mapper
interface CreditCardDtoConverter {
    fun convertDto(cCardDto: CreditCardCreateDto, id: UUID): CreditCard
}
