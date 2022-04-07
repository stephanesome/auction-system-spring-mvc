package seg3x02.auctionsystem.framework.web.forms.validators

import org.springframework.beans.BeanWrapperImpl
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PasswordMatchImpl: ConstraintValidator<PasswordMatch, Object> {
    lateinit var passwordField: String
    lateinit var passwordConfField: String

    override fun initialize(constraintAnnotation: PasswordMatch) {
        this.passwordField = constraintAnnotation.passwordField
        this.passwordConfField = constraintAnnotation.passwordConfField
    }

    override fun isValid(value: Object?, context: ConstraintValidatorContext?): Boolean {
        val passwordValue = value?.let { BeanWrapperImpl(it).getPropertyValue(passwordField) }
        val passwordConfValue = value?.let { BeanWrapperImpl(it).getPropertyValue(passwordConfField) }

        var isValid: Boolean = passwordValue == passwordConfValue

        if (!isValid) {
            context?.disableDefaultConstraintViolation()
            context?.buildConstraintViolationWithTemplate(context.defaultConstraintMessageTemplate)
                ?.addPropertyNode(this.passwordConfField)?.addConstraintViolation()
        }
        return isValid
    }
}
