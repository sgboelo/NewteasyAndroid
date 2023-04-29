package com.SmartTech.teasyNew.activity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by muddvayne on 21/06/2017.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

/**
 * Mark any BaseActivity subclass by this annotation to make it lockable
 * Lockable activity will be forcibly closed after some time of inactivity and ActivityLogin will be opened
 * */
public @interface Lockable {
}
