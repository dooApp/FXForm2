package com.dooapp.fxform.view.factory.impl;

import com.dooapp.fxform.AbstractFXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.impl.ReadOnlyPropertyFieldElement;
import com.dooapp.fxform.reflection.ReflectionUtils;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.control.map.MapEditorControl;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.util.Callback;

import java.lang.reflect.Field;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 24/04/15
 * Time: 15:41
 */
public class MapEditorControlFactory implements Callback<Void, FXFormNode> {

    public final static System.Logger logger = System.getLogger(MapEditorControlFactory.class.getName());

    @Override
    public FXFormNode call(Void param) {
        MapEditorControl mapEditorControl = new MapEditorControl();
        return new FXFormNode() {
            @Override
            public Property getProperty() {
                return mapEditorControl.mapProperty();
            }

            @Override
            public void init(Element element, AbstractFXForm fxForm) {
                if (element instanceof ReadOnlyPropertyFieldElement) {
                    Field field = ((ReadOnlyPropertyFieldElement) element).getField();
                    Class<?> mapValueType = ReflectionUtils.getMapPropertyValueType(field);
                    mapEditorControl.setValueType(mapValueType);
                } else {
                    logger.log(System.Logger.Level.WARNING, "Unable to extract MapProperty value type which is required by the MapEditorControlFactory for this element: " + element);
                }
            }

            @Override
            public boolean isEditable() {
                return true;
            }

            @Override
            public Node getNode() {
                return mapEditorControl;
            }

            @Override
            public void dispose() {
                mapEditorControl.dispose();
            }
        };
    }
}
