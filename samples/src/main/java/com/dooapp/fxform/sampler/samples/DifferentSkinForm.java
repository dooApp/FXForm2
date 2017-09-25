package com.dooapp.fxform.sampler.samples;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.sampler.FXFormSample;
import com.dooapp.fxform.sampler.Utils;
import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.sampler.model.Movies;
import com.dooapp.fxform.view.skin.DefaultSkin;
import com.dooapp.fxform.view.skin.InlineSkin;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * @author Bastien
 */
public class DifferentSkinForm extends FXFormSample {

    @Override
    public String getSampleName() {
        return "Different skin";
    }

    @Override
    public Node getPanel(Stage stage) {
        final BorderPane bp = new BorderPane();
        Label title = new Label("Inline skin");
        VBox box = new VBox(10);
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        User user = new User();

        bp.setStyle("-fx-padding: 10;");
        choiceBox.getItems().addAll("Inline skin", "Default skin");
        bp.setTop(choiceBox);
        title.setFont(Font.font(24));

        FXForm form = new FXFormBuilder<>()
                .includeAndReorder("firstName", "lastName", "age", "favoriteMovie", "coolDeveloper")
                .resourceBundle(Utils.SAMPLE)
                .build();
        form.setSource(user);
        box.getChildren().addAll(title, form);
        choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                if (s2.equals("Inline skin")) {
                    title.setText("Inline skin");
                    form.setSkin(new InlineSkin(form));
                } else {
                    title.setText("Default skin");
                    form.setSkin(new DefaultSkin(form));
                }
            }
        });
        bp.setCenter(box);
        choiceBox.getSelectionModel().selectFirst();
        return bp;
    }

    @Override
    public String getControlStylesheetURL() {
        return null;
    }

    @Accessor(value = Accessor.AccessType.FIELD)
    public class User {

        public StringProperty firstName = new SimpleStringProperty();
        public StringProperty lastName = new SimpleStringProperty();
        public IntegerProperty age = new SimpleIntegerProperty(10);
        public ObjectProperty<Movies> favoriteMovie = new SimpleObjectProperty<>();
        public BooleanProperty coolDeveloper = new SimpleBooleanProperty();
    }

    @Override
    public String getSampleDescription() {
        return "This is an example of how to use different skins with your form";
    }
}
