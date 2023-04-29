package com.SmartTech.teasyNew.utils;

/**
 * Created by muddvayne on 04/09/2017.
 */

public class CompositeElement<T> {

    private String tag;

    private T element;

    private int weight;

    public CompositeElement(String tag, T element, int weight) {
        this.tag = tag;
        this.element = element;
        this.weight = weight;
    }

    public String getTag() {
        return this.tag;
    }

    public T getElement() {
        return this.element;
    }

    public Integer getWeight() {
        return this.weight;
    }

}
