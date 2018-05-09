package lib.misc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation indicates that the value of this argument can be changed.
 * @author Hiroyuki Masuda
 */
@Target(ElementType.PARAMETER)
public @interface Variable {

}
