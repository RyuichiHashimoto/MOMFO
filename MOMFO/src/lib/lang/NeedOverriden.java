package lib.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method is intended to be overridden in a sub-class
 * except for some special cases (e.g., Mock Object).
 * This annotation is an extension of the <tt>abstract</tt> modifier.
 * This annotation should be added to a method which is not abstract but should
 * be overridden. Typical examples for such methods are <tt>clone</tt>,
 * <tt>toString</tt>, <tt>equals</tt> etc.
 *
 *
 * @author Hiroyuki MASUDA
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface NeedOverriden {}
