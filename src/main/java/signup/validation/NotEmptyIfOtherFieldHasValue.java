package signup.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validates that field {@code dependFieldName} is not empty if field
 * {@code fieldName} has value {@code fieldValue}.
 **/
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = NotEmptyIfOtherFieldHasValueValidator.class)
@Documented
public @interface NotEmptyIfOtherFieldHasValue {

    @Target({ TYPE, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        NotEmptyIfOtherFieldHasValue[] value();
    }

    String dependFieldName();

    String fieldName();

    String fieldValue();

    Class<?>[] groups() default {};

    String message() default "{NotEmptyIfOtherFieldHasValue.message}";

    Class<? extends Payload>[] payload() default {};

}
