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
 * <br>
 * Created at 27/03/14 17:00.<br>
 *
 * @author Bastien
 */
public class SimpleForm extends FXFormSample {

    @Override
    public String getSampleName() {
        return "Simple form";
    }

    @Override
    public Node getPanel(Stage stage) {
        Pane root = new Pane();

        FXForm form = new FXFormBuilder<>()
                .includeAndReorder("firstName", "lastName", "age", "favoriteMovie", "coolDeveloper")
                .resourceBundle(Utils.SAMPLE)
                .build();
        User user = new User();
        form.setSource(user);

        root.getChildren().add(form);
        return root;
    }

    @Override
    public String getSampleDescription() {
        return "This is an example to do a very basic form with a simple bean";
    }
}
