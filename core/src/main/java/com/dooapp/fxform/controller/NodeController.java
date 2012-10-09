package com.dooapp.fxform.controller;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.utils.Disposable;
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Created at 27/09/12 15:33.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public abstract class NodeController implements Disposable {

    private final ObjectProperty<FXFormNode> node = new SimpleObjectProperty<FXFormNode>();
    private final ChangeListener<FXFormNode> changeListener;
    private final Element element;
    private final FXForm fxForm;

    public NodeController(FXForm fxForm, Element element) {
        this.element = element;
        this.fxForm = fxForm;
        changeListener = new ChangeListener<FXFormNode>() {
            public void changed(ObservableValue<? extends FXFormNode> observableValue, FXFormNode fxFormNode, FXFormNode fxFormNode1) {
                if (fxFormNode != null) {
                    unbind(fxFormNode);
                    fxFormNode.dispose();
                }
                if (fxFormNode1 != null) {
                    fxFormNode1.init(NodeController.this.element);
                    bind(fxFormNode1);
                }
            }
        };
        node.addListener(changeListener);
    }

    protected abstract void bind(FXFormNode fxFormNode);

    protected void unbind(FXFormNode fxFormNode) {
        fxFormNode.getProperty().unbind();
    }

    public void dispose() {
        node.removeListener(changeListener);
    }

    public FXFormNode getNode() {
        return node.get();
    }

    public void setNode(FXFormNode node1) {
        node.set(node1);
    }

    public ObjectProperty<FXFormNode> nodeProperty() {
        return node;
    }

    protected Element getElement() {
        return element;
    }

    protected FXForm getFxForm() {
        return fxForm;
    }
}