package seg3x02.auctionsystem.adapters.repositories.converters

import org.mapstruct.Mapper
import seg3x02.auctionsystem.domain.user.entities.creditCard.Address
import seg3x02.auctionsystem.domain.user.entities.creditCard.CreditCard
import seg3x02.auctionsystem.framework.jpa.entities.user.creditCard.AddressJpaEntity
import seg3x02.auctionsystem.framework.jpa.entities.user.creditCard.CreditCardJpaEntity

@Mapper
interface CreditCardJpaConverter {
    fun convertToJpa(creditCard: CreditCard): CreditCardJpaEntity
    fun convertAddressToJpa(address: Address): AddressJpaEntity
    fun convertToModel(creditCardJpa: CreditCardJpaEntity): CreditCard
    fun convertAddressToModel(address: AddressJpaEntity): Address
}
