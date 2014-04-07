package com.dooapp.fxform.samples;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.FXFormSample;
import com.dooapp.fxform.Utils;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.model.User;
import com.dooapp.fxform.reflection.MultipleBeanSource;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * TODO write documentation<br>
 *<br>
 * Created at 04/04/14 14:19.<br>
 *
 * @author Bastien
 *
 */
public class MultiBeanForm extends FXFormSample {

    @Override
    public String getSampleName() {
        return "Multiple bean form";
    }

    @Override
    public Node getPanel(Stage stage) {
        Pane root = new Pane();

        FXForm form = new FXFormBuilder<>()
                .includeAndReorder("firstName", "lastName", "age", "favoriteMovie", "coolDeveloper", "street", "city", "zip")
                .resourceBundle(Utils.SAMPLE)
                .build();
        User user = new User();
        form.setSource(new MultipleBeanSource(user, user.getAddress()));

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
        return "This is an example to do a very basic form with more than one bean";
    }
}
