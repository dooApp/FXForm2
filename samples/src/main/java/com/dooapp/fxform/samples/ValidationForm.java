package com.dooapp.fxform.samples;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.FXFormSample;
import com.dooapp.fxform.Utils;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.model.UserWithConstraintsValidation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * TODO write documentation<br>
 *<br>
 * Created at 11/04/14 16:16.<br>
 *
 * @author Bastien
 *
 */
public class ValidationForm extends FXFormSample {


    @Override
    public String getSampleName() {
        return "Simple validation form";
    }

    @Override
    public Node getPanel(Stage stage) {
        Pane root = new Pane();

        FXForm form = new FXFormBuilder<>()
                .includeAndReorder("firstName", "lastName", "age", "favoriteMovie", "coolDeveloper")
                .resourceBundle(Utils.SAMPLE)
                .build();
        UserWithConstraintsValidation user = new UserWithConstraintsValidation();
        form.setSource(user);

        root.getChildren().add(form);
        return root;
    }

    @Override
    public String getSampleDescription() {
        return "A form with validation, age must be greater than 5";
    }
}
