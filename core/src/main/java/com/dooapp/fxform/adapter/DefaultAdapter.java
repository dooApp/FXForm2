package com.dooapp.fxform.adapter;

/**
 * Created at 27/09/12 11:08.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class DefaultAdapter implements Adapter<Object, Object> {

    public Object adaptTo(Object from) {
        return from;
    }

    public Object adaptFrom(Object to) {
        return to;
    }
}