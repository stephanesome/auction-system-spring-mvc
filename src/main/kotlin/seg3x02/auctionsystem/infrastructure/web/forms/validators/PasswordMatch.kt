package seg3x02.auctionsystem.infrastructure.web.forms.validators

import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Constraint(validatedBy = [PasswordMatchImpl::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class PasswordMatch(
    val message: String = "Password do not Match",
    val passwordField: String,
    val passwordConfField: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [])
