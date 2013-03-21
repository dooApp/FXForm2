package com.dooapp.fxform.view.factory;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.factory.impl.AutoHidableLabelFactory;
import javafx.util.Callback;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 21/03/13
 * Time: 09:36
 */
public class DefaultTooltipFactoryProvider implements FactoryProvider {

    public Callback<Void, FXFormNode> getFactory(Element element) {
        return new AutoHidableLabelFactory();
    }

}
