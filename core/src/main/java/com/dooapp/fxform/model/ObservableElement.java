package com.dooapp.fxform.model;

import com.dooapp.fxform.annotation.FormFactory;
import javafx.beans.value.ObservableValue;

/**
 * Created at 30/08/12 13:55.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public interface ObservableElement<T> extends ObservableValue<T> {

    /**
     * Get the name of this element
     *
     * @return
     */
    public String getName();

    /**
     * Dispose this element
     */
    public void dispose();

    /**
     * Get the exact type of this element
     *
     * @return
     */
    public Class<? extends ObservableValue<T>> getType();

    /**
     * Get the encapsulated type of the observable value
     *
     * @return
     */
    public Class<? extends T> getValueType();

}
