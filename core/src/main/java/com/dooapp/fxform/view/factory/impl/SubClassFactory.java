package com.dooapp.fxform.view.factory.impl;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import com.dooapp.fxform.view.factory.FactoryProvider;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.lang.annotation.Annotation;

/**
 * A factory that call the default factory provider depending on wrapped type at runtime.
 * <p/>
 * User: Kevin Senechal <kevin.senechal@dooapp.com>
 * Date: 12/08/2015
 * Time: 11:00
 */
public class SubClassFactory implements Callback<Void, FXFormNode> {

    private final FactoryProvider factoryProvider;

    public SubClassFactory(FactoryProvider factoryProvider) {
        this.factoryProvider = factoryProvider;
    }

    @Override
    public FXFormNode call(Void param) {
        final StackPane stackPane = new StackPane();
        final ObjectProperty p = new SimpleObjectProperty();
        return new FXFormNodeWrapper(stackPane, p) {
            public ChangeListener elementChangeListener;
            private Element element;
            private FXFormNode node;

            @Override
            public void init(Element element) {
                super.init(element);
                this.element = element;
                elementChangeListener = (observable, oldValue, newValue) -> {
                    stackPane.getChildren().clear();
                    if (node != null) {
                        node.getProperty().setValue(null);
                        node.dispose();
                        node = null;
                    }
                    if (newValue != null) {
                        Callback<Void, FXFormNode> factory = factoryProvider.getFactory(new ClassElement(newValue.getClass()));
                        if (factory != null) {
                            node = factory.call(null);
                            node.init(element);
                            node.getProperty().setValue(newValue);
                            stackPane.getChildren().add(node.getNode());
                        } else {
                            stackPane.getChildren().add(new Label(newValue.toString()));
                        }
                    }
                };
                element.addListener(elementChangeListener);
                if (element.getValue() != null) {
                    Callback<Void, FXFormNode> factory = factoryProvider.getFactory(new ClassElement(element.getValue().getClass()));
                    if (factory != null) {
                        node = factory.call(null);
                        node.init(element);
                        node.getProperty().setValue(element.getValue());
                        stackPane.getChildren().add(node.getNode());
                    } else {
                        stackPane.getChildren().add(new Label(element.getValue().toString()));
                    }
                }
            }

            @Override
            public void dispose() {
                element.removeListener(elementChangeListener);
                elementChangeListener = null;
                element = null;
                if (node != null) {
                    if (node.getProperty() != null) {
                        node.getProperty().setValue(null);
                    }
                    node.dispose();
                    node = null;
                }
                super.dispose();
            }
        };
    }

    private class ClassElement implements Element {
        private final Class clazz;

        public ClassElement(Class clazz) {
            this.clazz = clazz;
        }

        @Override
        public Class<?> getType() {
            return clazz;
        }

        @Override
        public Class getWrappedType() {
            return clazz;
        }

        @Override
        public Property sourceProperty() {
            return null;
        }

        @Override
        public Class getDeclaringClass() {
            return null;
        }

        @Override
        public String getCategory() {
            return null;
        }

        @Override
        public void setCategory(String s) {

        }

        @Override
        public Annotation getAnnotation(Class aClass) {
            return null;
        }

        @Override
        public void dispose() {

        }

        @Override
        public Object getBean() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public void addListener(ChangeListener listener) {

        }

        @Override
        public void removeListener(ChangeListener listener) {

        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public void addListener(InvalidationListener listener) {

        }

        @Override
        public void removeListener(InvalidationListener listener) {

        }
    }
}
