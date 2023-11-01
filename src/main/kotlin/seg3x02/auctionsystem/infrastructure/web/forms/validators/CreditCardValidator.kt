package seg3x02.auctionsystem.infrastructure.web.forms.validators

import jakarta.validation.Constraint
import kotlin.reflect.KClass


@Constraint(validatedBy = [CreditCardValidatorImpl::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CreditCardValidator(
    val message: String = "Incomplete credit card information",
    val numberField: String,
    val mandatoryField: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []

) {
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class List(vararg val value: CreditCardValidator)
}

