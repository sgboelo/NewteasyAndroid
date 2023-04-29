package com.SmartTech.teasyNew.view;

import android.view.View;

import com.SmartTech.teasyNew.utils.CompositeElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by muddvayne on 04/09/2017.
 */

public class CompositeOnClickListener implements View.OnClickListener {

    private final int DEFAULT_LISTENER_ORDER;

    private List<CompositeElement<View.OnClickListener>> listeners = new ArrayList<>();

    /**
     * CompositeOnClickListener may contain multiple onClick listeners
     * which will be executed all one by one in specified order
     *
     * listeners will be executed one by one.
     * listeners with smaller order will be executed first.
     *
     * if you will not specify execution order for listener
     * in addListener methods, then default value will be used for them.
     * you can specify default order value for listeners added in such way
     * in CompositeOnClickListener(int defaultListenerOrder) constructor
     * (this constructor will set this value to 100)
     * */
    public CompositeOnClickListener() {
        DEFAULT_LISTENER_ORDER = 100;
    }

    /**
     * CompositeOnClickListener may contain multiple onClick listeners
     * which will be executed all one by one in specified order.
     *
     * listeners will be executed one by one.
     * listeners with smaller order will be executed first.
     *
     * @param defaultListenerOrder if you will not specify execution order for listener
     *                             in addListener methods, then this default value will be used for them
     * */
    public CompositeOnClickListener(int defaultListenerOrder) {
        DEFAULT_LISTENER_ORDER = defaultListenerOrder;
    }

    /**
     * adds listener to the queue. all listeners will be executed in specified order
     * @param tag you can use it later to remove all listeners from the queue with same tag
     * @param order listeners will be executed one by one.
     *              listeners with smaller order will be executed first.
     * */
    public void addListener(String tag, View.OnClickListener listener, int order) {
        this.listeners.add(new CompositeElement<>(tag, listener, order));
        if(listeners.size() > 1) {
            Collections.sort(listeners, listenerComparator);
        }
    }

    private static final Comparator<CompositeElement<View.OnClickListener>> listenerComparator =
            new Comparator<CompositeElement<View.OnClickListener>>() {

                @Override
                public int compare(CompositeElement<View.OnClickListener> o1, CompositeElement<View.OnClickListener> o2) {
                    Integer weight1 = o1.getWeight();
                    Integer weight2 = o2.getWeight();
                    return weight1.compareTo(weight2);
                }
            };

    /**
     * adds listener to the queue. all listeners will be executed in specified order
     * @param tag you can use it later to remove all listeners from the queue with same tag
     *
     * listener will get default execution order
     * */
    public void addListener(String tag, View.OnClickListener listener) {
        addListener(tag, listener, DEFAULT_LISTENER_ORDER);
    }

    /**
     * adds listener to the queue. all listeners will be executed in specified order
     * listener will be tagged by empty string and will get default execution order
     * */
    public void addListener(View.OnClickListener listener) {
        addListener("", listener, DEFAULT_LISTENER_ORDER);
    }

    /**
     * @return true if listener removed successfully; false if this listener was not registered
     * */
    public boolean removeListener(View.OnClickListener listener) {
        for (CompositeElement<View.OnClickListener> object : listeners) {
            if(listener == object.getElement()) {
                this.listeners.remove(object);
                return true;
            }
        }

        return false;
    }

    /**
     * @param tag use it to remove all listeners with same tags
     * @return amount of listeners that was removed
     * */
    public int removeAllListenersWithTag(String tag) {
        int removed = 0;
        for (CompositeElement<View.OnClickListener> object : listeners) {
            if(tag.equals(object.getTag())) {
                listeners.remove(object);
                ++removed;
            }
        }

        return removed;
    }

    /**
     * @return amount of listeners that was removed
     * */
    public int removeAllListeners() {
        int size = listeners.size();
        listeners = new ArrayList<>();
        return size;
    }

    @Override
    public void onClick(View v) {
        for (CompositeElement<View.OnClickListener> object : listeners) {
            View.OnClickListener listener = object.getElement();
            listener.onClick(v);
        }
    }
}
