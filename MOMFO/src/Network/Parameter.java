package Network;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/*
 * This class perform like the hashmap.
 *  
 * 
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Parameter {
	public static final String NOT_SPECIFIED = "__null__";

	String name() default NOT_SPECIFIED;
	boolean required() default false;
	String description() default "";
}
