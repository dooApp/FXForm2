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
 * Created at 11/04/14 15:17.<br>
 *
 * @author Bastien
 *
 */
public class CuteCssForm extends FXFormSample {

    @Override
    public String getSampleName() {
        return "Form with css";
    }

    @Override
    public Node getPanel(Stage stage) {
        Pane root = new Pane();
        FXForm form = new FXFormBuilder<>()
                .includeAndReorder("firstName", "lastName", "age", "favoriteMovie", "coolDeveloper")
                .resourceBundle(Utils.SAMPLE)
                .build();
        form.getStylesheets().add("form.css");

        User user = new User();
        form.setSource(user);

        root.getChildren().add(form);
        return root;
    }

    @Override
    public String getSampleDescription() {
        return "A form with really cool colors";
    }
}
