package scw.app.editable.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import scw.app.editable.InputType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Input {
	InputType type() default InputType.INPUT;
}
