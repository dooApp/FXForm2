package com.dooapp.fxform.view.control.map;

import com.dooapp.fxform.model.Element;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.MapProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.MapChangeListener;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 24/04/15
 * Time: 15:01
 */
public class MapEntryElement<K, V> implements Element<V> {

    protected class EntryMapChangeListener implements MapChangeListener<K, V> {
        @Override
        public void onChanged(Change<? extends K, ? extends V> change) {
            if (key.equals(change.getKey())) {
                for (ChangeListener changeListener : changeListeners) {
                    changeListener.changed(MapEntryElement.this, null, map.get(key));
                }
            }
        }
    }

    protected final MapProperty<K, V> map;

    protected final K key;

    private final Class<V> valueType;

    private final StringProperty category = new SimpleStringProperty();

    private final List<ChangeListener> changeListeners = new ArrayList<>();

    private final List<InvalidationListener> invalidationListeners = new ArrayList<>();

    private final MapChangeListener<K, V> changeListener = new MapChangeListener<K, V>() {
        @Override
        public void onChanged(Change<? extends K, ? extends V> change) {
            if (key.equals(change.getKey())) {
                for (ChangeListener changeListener : changeListeners) {
                    changeListener.changed(MapEntryElement.this, null, map.get(key));
                }
            }
        }
    };

    private final InvalidationListener invalidationListener = new InvalidationListener() {

        @Override
        public void invalidated(Observable observable) {
            for (InvalidationListener invalidationListener : invalidationListeners) {
                invalidationListener.invalidated(MapEntryElement.this);
            }
        }
    };

    public MapEntryElement(MapProperty<K, V> map, K key, Class<V> valueType) {
        this.map = map;
        this.key = key;
        this.valueType = valueType;
        map.addListener(new MapChangeListener<K, V>() {
            @Override
            public void onChanged(Change<? extends K, ? extends V> change) {
                if (key.equals(change.getKey())) {
                    for (ChangeListener changeListener : changeListeners) {
                        changeListener.changed(MapEntryElement.this, null, map.get(key));
                    }
                }
            }
        });
        map.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                for (InvalidationListener invalidationListener : invalidationListeners) {
                    invalidationListener.invalidated(MapEntryElement.this);
                }
            }
        });
    }

    public K getKey() {
        return key;
    }

    @Override
    public Class<?> getType() {
        return StringProperty.class;
    }

    @Override
    public Class getWrappedType() {
        return valueType;
    }

    @Override
    public Property sourceProperty() {
        return map;
    }

    @Override
    public Class getDeclaringClass() {
        return MapProperty.class;
    }

    @Override
    public String getCategory() {
        return category.get();
    }

    @Override
    public void setCategory(String category) {
        this.category.setValue(category);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return null;
    }

    @Override
    public void dispose() {
        map.removeListener(changeListener);
        //map.removeListener(invalidationListener);
    }

    @Override
    public Object getBean() {
        return map;
    }

    @Override
    public String getName() {
        return key.toString();
    }

    @Override
    public void addListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }

    @Override
    public V getValue() {
        return map.get(key);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListeners.remove(listener);
    }

}
