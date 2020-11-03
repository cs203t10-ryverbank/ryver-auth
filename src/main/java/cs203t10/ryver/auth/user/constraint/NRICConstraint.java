package cs203t10.ryver.auth.user.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.springframework.validation.annotation.Validated;

@Documented
@Constraint(validatedBy = NRICValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Validated
public @interface NRICConstraint {
	String message() default "NRIC is not valid.";
 	Class<?>[] groups() default {};
 	Class<? extends Payload>[] payload() default {};
}