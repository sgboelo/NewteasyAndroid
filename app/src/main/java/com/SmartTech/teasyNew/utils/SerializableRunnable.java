package com.SmartTech.teasyNew.utils;

import androidx.annotation.CallSuper;

import java.io.Serializable;

/**
 * Created by muddvayne on 09/09/2017.
 */

/**
 * This runnable is intended to be created, serialized and executed in different class types
 * and get access to target object. Make sure to call setTargetObject() before run() in target object
 * */
public abstract class SerializableRunnable<T> implements Runnable, Serializable {

    private transient T targetObject;

    @Override
    @CallSuper
    public void run() {
        if(targetObject == null) {
            throw new IllegalStateException("Target object is null. Make sure to call " +
                    "setTargetObject() before run()");
        }
    }

    public T getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(T targetObject) {
        this.targetObject = targetObject;
    }
}
