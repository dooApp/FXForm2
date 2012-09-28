package com.dooapp.fxform.view.factory;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import javafx.util.Callback;

/**
 * Created at 28/09/12 10:45.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public interface FactoryProvider {

    public Callback<Void, FXFormNode> getFactory(Element element);

}