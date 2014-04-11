package com.dooapp.fxform.samples;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.FXFormSample;
import com.dooapp.fxform.Utils;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.model.User;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * TODO write documentation<br>
 *<br>
 * Created at 11/04/14 12:56.<br>
 *
 * @author Bastien
 *
 */
public class FXMLSkin extends FXFormSample {

    @Override
    public String getSampleName() {
        return "FXML Skin";
    }

    @Override
    public Node getPanel(Stage stage) {
        Pane root = new Pane();

        FXForm form = new FXFormBuilder<>().include("lastName", "firstName", "age").resourceBundle(Utils.SAMPLE).build();
        form.setSkin(new com.dooapp.fxform.view.skin.FXMLSkin(form, getClass().getResource("/fxmlSkin.fxml")));
        User user = new User();
        form.setSource(user);
        root.getChildren().add(form);
        return root;
    }

    @Override
    public String getJavaDocURL() {
        return "";
    }

    @Override
    public String getSampleSourceURL() {
        return "";
    }

    @Override
    public String getSampleDescription() {
        return "This is an example to use FXForm with FXML";
    }
}
