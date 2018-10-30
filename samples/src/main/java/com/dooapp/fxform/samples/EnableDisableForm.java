package com.dooapp.fxform.samples;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.FXFormSample;
import com.dooapp.fxform.Utils;
import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.model.Movies;
import com.dooapp.fxform.view.factory.DisableFactoryProviderWrapper;
import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 30/10/2018
 * Time: 13:37
 */
public class EnableDisableForm extends FXFormSample {

    @Accessor(value = Accessor.AccessType.FIELD)
    public class User {

        public StringProperty firstName = new SimpleStringProperty();
        public StringProperty lastName = new SimpleStringProperty();
        public IntegerProperty age = new SimpleIntegerProperty(10);
        public ObjectProperty<Movies> favoriteMovie = new SimpleObjectProperty<>();
        public BooleanProperty coolDeveloper = new SimpleBooleanProperty();
    }

    @Override
    public String getSampleName() {
        return "Enable/disable form";
    }

    @Override
    public Node getPanel(Stage stage) {
        VBox root = new VBox();
        FXForm form = new FXFormBuilder<>()
                .includeAndReorder("firstName", "lastName", "age", "favoriteMovie")
                .resourceBundle(Utils.SAMPLE)
                .build();
        DisableFactoryProviderWrapper factoryProvider = new DisableFactoryProviderWrapper(form.getEditorFactoryProvider());
        form.setEditorFactoryProvider(factoryProvider);
        User user = new User();
        user.firstName.set("Robert");
        user.lastName.set("Shepard");
        user.age.setValue(42);
        user.favoriteMovie.setValue(Movies.LOTR);
        form.setSource(user);
        FXForm optionForm = new FXFormBuilder<>()
                .include("disableProperty")
                .resourceBundle(Utils.SAMPLE)
                .source(factoryProvider)
                .build();
        root.getChildren().addAll(optionForm, form);
        return root;
    }

    @Override
    public String getSampleDescription() {
        return "The editors of this form can be easily enabled/disabled";
    }

}
