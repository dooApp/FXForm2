package com.dooapp.fxform.view.control.map;

import com.dooapp.fxform.model.Element;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakMapChangeListener;

import java.util.stream.Collectors;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 24/04/15
 * Time: 14:52
 */
public class MapElementProvider<K, V> {

    private final ListProperty<Element<V>> elements = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final MapProperty<K, V> map;

    private final Class<V> valueType;
    private final MapChangeListener<K, V> mapChangeListener;

    public MapElementProvider(MapProperty<K, V> map, Class<V> valueType) {
        this.map = map;
        this.valueType = valueType;
        mapChangeListener = change -> {
            if (change.wasAdded()) {
                if (getElementByKey(change.getKey()) == null) {
                    elements.add(new MapEntryPropertyElement(map, change.getKey(), valueType));
                }
            } else if (change.wasRemoved()) {
                elements.remove(getElementByKey(change.getKey()));
            }
        };
        map.addListener(new WeakMapChangeListener<>(mapChangeListener));
        init();
    }

    private Element getElementByKey(K key) {
        Element toRemove = null;
        for (Element element : elements) {
            if (((MapEntryElement) element).getKey().equals(key)) {
                toRemove = element;
                break;
            }
        }
        return toRemove;
    }

    protected void init() {
        elements.addAll(map.keySet().stream()
                .map(key -> new MapEntryPropertyElement<>(map, key, valueType))
                .collect(Collectors.toList()));
    }

    public ObservableList<Element<V>> getElements() {
        return elements.get();
    }

    public ReadOnlyListProperty<Element<V>> elementsProperty() {
        return elements;
    }

    public void dispose() {
        map.removeListener(mapChangeListener);
    }

}
