package com.dooapp.fxform.view.control.map;

import com.dooapp.fxform.model.Element;
import javafx.beans.binding.ListBinding;
import javafx.beans.property.MapProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 24/04/15
 * Time: 14:52
 */
public class MapElementBinding<K, V> extends ListBinding<Element> {

    private final MapProperty<K, V> map;

    private final Class<V> valueType;

    public MapElementBinding(MapProperty<K, V> map, Class<V> valueType) {
        this.map = map;
        this.valueType = valueType;
        map.addListener((MapChangeListener<K, V>) change -> {
            if (change.wasAdded() || change.wasRemoved()) {
                invalidate();
            }
        });
    }

    @Override
    protected ObservableList<Element> computeValue() {
        ObservableList<Element> list = FXCollections.observableArrayList();
        for (K key : map.keySet()) {
            list.add(new MapEntryPropertyElement<>(map, key, valueType));
        }
        return list;
    }
}
