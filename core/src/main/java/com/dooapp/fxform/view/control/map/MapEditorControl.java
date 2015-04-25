package com.dooapp.fxform.view.control.map;

import com.dooapp.fxform.AbstractFXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.validation.FXFormValidator;
import com.dooapp.fxform.view.skin.InlineSkin;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import java.util.Collections;
import java.util.List;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 24/04/15
 * Time: 14:48
 */
public class MapEditorControl<T> extends AbstractFXForm {

    private MapElementProvider mapElementProvider;

    public MapEditorControl() {
        super();
        // no validation can be performed
        setFxFormValidator(new FXFormValidator() {
            @Override
            public List<ConstraintViolation> validate(Element element, Object newValue, Class... groups) {
                return Collections.emptyList();
            }

            @Override
            public List<ConstraintViolation> validateClassConstraint(Object bean) {
                return Collections.emptyList();
            }

            @Override
            public MessageInterpolator getMessageInterpolator() {
                return null;
            }
        });
        // use the InlineSkin by default
        setSkin(new InlineSkin(this));
    }

    private final MapProperty<String, T> map = new SimpleMapProperty<>(FXCollections.observableHashMap());

    public void setValueType(Class<T> valueType) {
        mapElementProvider = new MapElementProvider(map, valueType);
        elementsProperty().bind(mapElementProvider.elementsProperty());
    }

    public ObservableMap<String, T> getMap() {
        return map.get();
    }

    public MapProperty<String, T> mapProperty() {
        return map;
    }

    public void setMap(ObservableMap<String, T> map) {
        this.map.set(map);
    }

    public void dispose() {
        elementsProperty().unbind();
        elementsProperty().clear();
        mapElementProvider.dispose();
    }
}
