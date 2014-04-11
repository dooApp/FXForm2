package com.dooapp.fxform.samples;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.FXFormSample;
import com.dooapp.fxform.Utils;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.model.UserWithCustomFactory;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * TODO write documentation<br>
 *<br>
 * Created at 11/04/14 14:14.<br>
 *
 * @author Bastien
 *
 */
public class CustomFactoryForm extends FXFormSample {


    @Override
    public String getSampleName() {
        return "Custom factory form";
    }

    @Override
    public Node getPanel(Stage stage) {
        Pane root = new Pane();
        FXForm form = new FXFormBuilder<>().includeAndReorder("firstName", "lastName", "age").resourceBundle(Utils.SAMPLE).build();
        UserWithCustomFactory userWithCustomFactory = new UserWithCustomFactory();
        form.setSource(userWithCustomFactory);
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
        return "This is an example to use FXForm with custom Factory. Here the default lastName TextField is replaced by a TextArea";
    }
}
