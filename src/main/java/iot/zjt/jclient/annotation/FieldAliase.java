package iot.zjt.jclient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The aliase of fields
 * @version 2018.10.28
 * @author Mr Dk.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldAliase {
    String value();
}
