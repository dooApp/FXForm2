package com.dooapp.fxform.view.control.map;

import com.dooapp.fxform.model.PropertyElement;
import javafx.beans.property.MapProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 24/04/15
 * Time: 16:51
 */
public class MapEntryPropertyElement<K, V> extends MapEntryElement<K, V> implements PropertyElement<V> {

    private ChangeListener<V> changeListener = (observable1, oldValue, newValue) -> map.put(key, newValue);

    private ObservableValue<? extends V> observable;

    public MapEntryPropertyElement(MapProperty<K, V> map, K key, Class<V> valueType) {
        super(map, key, valueType);
    }

    @Override
    public void bind(ObservableValue<? extends V> observable) {
        if (observable == null) {
            this.observable = observable;
            observable.addListener(changeListener);
        }
    }

    @Override
    public void unbind() {
        if (observable != null) {
            observable.removeListener(changeListener);
            observable = null;
        }
    }

    @Override
    public boolean isBound() {
        return observable != null;
    }

    @Override
    public void bindBidirectional(Property<V> other) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void unbindBidirectional(Property<V> other) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setValue(V value) {
        map.put(key, value);
    }

}
