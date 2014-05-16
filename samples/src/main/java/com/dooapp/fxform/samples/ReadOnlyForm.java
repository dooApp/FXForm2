package com.dooapp.fxform.samples;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.FXFormSample;
import com.dooapp.fxform.Utils;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.model.Movies;
import com.dooapp.fxform.model.User;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * TODO write documentation<br>
 *<br>
 * Created at 04/04/14 14:50.<br>
 *
 * @author Bastien
 *
 */
public class ReadOnlyForm extends FXFormSample {

    @Override
    public String getSampleName() {
        return "Read only form";
    }

    @Override
    public Node getPanel(Stage stage) {
        Pane root = new Pane();

        FXForm form = new FXFormBuilder<>()
                .includeAndReorder("firstName", "lastName", "age", "favoriteMovie")
                .resourceBundle(Utils.SAMPLE)
                .readOnly(true)
                .build();

        User user = new User();
        user.setFirstName("Robert");
        user.setLastName("Shepard");
        user.setAge(42);
        user.setFavoriteMovie(Movies.LOTR);
        form.setSource(user);

        root.getChildren().add(form);
        return root;
    }

    @Override
    public String getSampleDescription() {
        return "This is how you can do a read only form";
    }
}
