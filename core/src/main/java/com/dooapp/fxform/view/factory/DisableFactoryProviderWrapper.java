package com.dooapp.fxform.view.factory;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Callback;

/**
 * A wrapper around {@link FactoryProvider} that allow to easily enable/disable the state of the provided nodes.
 * <p>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 30/10/2018
 * Time: 13:28
 */
public class DisableFactoryProviderWrapper implements FactoryProvider {

    final BooleanProperty disableProperty = new SimpleBooleanProperty();

    private final FactoryProvider factoryProvider;

    public DisableFactoryProviderWrapper(FactoryProvider factoryProvider) {
        this.factoryProvider = factoryProvider;
    }

    @Override
    public Callback<Void, FXFormNode> getFactory(Element element) {
        Callback<Void, FXFormNode> result = factoryProvider.getFactory(element);
        return param -> {
            FXFormNode fxFormNode = result.call(param);
            fxFormNode.getNode().disableProperty().bind(disableProperty);
            return fxFormNode;
        };
    }

    public boolean getDisableProperty() {
        return disableProperty.get();
    }

    public BooleanProperty disablePropertyProperty() {
        return disableProperty;
    }

    public void setDisableProperty(boolean disableProperty) {
        this.disableProperty.set(disableProperty);
    }
}
