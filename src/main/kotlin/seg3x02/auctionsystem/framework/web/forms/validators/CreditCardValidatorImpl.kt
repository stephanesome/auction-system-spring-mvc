package seg3x02.auctionsystem.framework.web.forms.validators

import org.springframework.beans.BeanWrapperImpl
import java.lang.ClassCastException
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CreditCardValidatorImpl: ConstraintValidator<CreditCardValidator, Object> {
    lateinit var numberField: String
    lateinit var mandatoryField: String

    override fun initialize(constraintAnnotation: CreditCardValidator) {
        this.numberField = constraintAnnotation.numberField
        this.mandatoryField = constraintAnnotation.mandatoryField
    }

    override fun isValid(value: Object?, context: ConstraintValidatorContext?): Boolean {
        val numberFieldValue = value?.let { BeanWrapperImpl(it).getPropertyValue(numberField) }
        val mandatoryFieldValue = value?.let { BeanWrapperImpl(it).getPropertyValue(mandatoryField) }

        var isValid: Boolean = true
        if (numberFieldValue != null) {
            val numberValueStr = numberFieldValue as String
            if (numberValueStr.isNotEmpty()) {
                isValid = if (mandatoryFieldValue == null) {
                    false
                } else {
                    try {
                        val mandatoryFieldValueStr = mandatoryFieldValue as String
                        mandatoryFieldValueStr.isNotEmpty()
                    }
                    catch (exp: ClassCastException) {
                        true
                    }
                }
            }
        }
        if (!isValid) {
            context?.disableDefaultConstraintViolation()
            context?.buildConstraintViolationWithTemplate(context.defaultConstraintMessageTemplate)
                ?.addPropertyNode(this.mandatoryField)?.addConstraintViolation()
        }
        return isValid
    }
}
