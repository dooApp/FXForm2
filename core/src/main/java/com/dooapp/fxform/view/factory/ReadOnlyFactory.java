package com.dooapp.fxform.view.factory;

import com.dooapp.fxform.model.ElementController;
import com.dooapp.fxform.view.NodeCreationException;
import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * Factory creating a label bound to a string representation of the {@code Element}<br>
 * <br>
 * Created at 20/09/11 10:45.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class ReadOnlyFactory implements NodeFactory {

    private final FormatProvider formatProvider;

    public ReadOnlyFactory() {
        this(new FormatProviderImpl());
    }

    public ReadOnlyFactory(FormatProvider formatProvider) {
        this.formatProvider = formatProvider;
    }

    public Node createNode(final ElementController controller) throws NodeCreationException {
        Label label = new Label();
        label.textProperty().bind(new StringBinding() {

            {
                bind(controller);
            }

            @Override
            protected String computeValue() {
                if (controller.getValue() != null) {
                    return formatProvider.getFormat(controller.getElement()).format(controller.getElement().getValue());
                } else {
                    return "";
                }
            }
        });
        return label;
    }
}