package com.SmartTech.teasyNew.activity.annotation;

import com.SmartTech.teasyNew.activity.base.BaseActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by muddvayne on 25/12/2017.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

/**
 * Activity marked by this annotation will override onSupportNavigateUp() and onBackPressed()
 * so that the given activity will be called
 * */
public @interface ReturnToActivity {
    Class<? extends BaseActivity> activity();

    /**
     * If set to true, then getIntent().getExtras() will be passed to the new intent
     * which will start activity.
     *
     * This may be useful, for instance, if you go Send Money -> To Teasy, then press back
     * and you need to restore Send Money activity's fields values
     * */
    boolean passExtras() default false;
}