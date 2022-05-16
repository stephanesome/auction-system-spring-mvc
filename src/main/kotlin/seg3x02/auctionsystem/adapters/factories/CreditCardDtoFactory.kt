package seg3x02.auctionsystem.adapters.factories

import org.mapstruct.factory.Mappers
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import seg3x02.auctionsystem.adapters.dtos.queries.CreditCardCreateDto
import seg3x02.auctionsystem.adapters.dtos.queries.converters.CreditCardDtoConverter
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.factories.CreditCardFactory
import java.util.*

@Primary
@Component
class CreditCardDtoFactory: CreditCardFactory {
    private val dtoConverter = Mappers.getMapper(CreditCardDtoConverter::class.java)

    override fun createCreditCard(creditCardInfo: CreditCardCreateDto): CreditCard {
        return dtoConverter.convertDto(creditCardInfo, UUID.randomUUID())
    }
}
