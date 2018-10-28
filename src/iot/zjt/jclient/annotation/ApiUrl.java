package iot.zjt.jclient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version 2018.10.27
 * @author Mr Dk.
 * The URL of the Kismet API for each type of information
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiUrl {
    String value();
}
