package com.dooapp.fxform.adapter;

/**
 * Created at 27/09/12 10:57.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public interface Adapter<T, V> {

    public V adaptTo(T from);

    public T adaptFrom(V to);

}
