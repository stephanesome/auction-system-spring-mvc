package seg3x02.auctionsystem.adapters.factories

import org.mapstruct.factory.Mappers
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import seg3x02.auctionsystem.adapters.dtos.CreditCardDto
import seg3x02.auctionsystem.adapters.dtos.converters.CreditCardDtoConverter
import seg3x02.auctionsystem.domain.user.core.creditCard.CreditCard
import seg3x02.auctionsystem.domain.user.factories.CreditCardFactory
import java.util.*

@Primary
@Component
class CreditCardDtoFactory: CreditCardFactory {
    private val dtoConverter = Mappers.getMapper(CreditCardDtoConverter::class.java)

    override fun createCreditCard(creditCardInfo: CreditCardDto): CreditCard {
        return dtoConverter.convertDto(creditCardInfo, UUID.randomUUID())
    }
}
