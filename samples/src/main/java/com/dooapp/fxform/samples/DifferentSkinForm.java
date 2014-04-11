package com.dooapp.fxform.samples;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.FXFormSample;
import com.dooapp.fxform.Utils;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.model.User;
import com.dooapp.fxform.view.skin.DefaultSkin;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * TODO write documentation<br>
 *<br>
 * Created at 04/04/14 14:33.<br>
 *
 * @author Bastien
 *
 */
public class DifferentSkinForm extends FXFormSample {

    @Override
    public String getSampleName() {
        return "Different skin";
    }

    @Override
    public Node getPanel(Stage stage) {
        final BorderPane bp = new BorderPane();
        bp.setStyle("-fx-padding: 10;");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("first", "second");
       bp.setTop(choiceBox);
        choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                VBox box = new VBox(10);

                if (s2.equals("first")) {
                    /**
                     ** INLINE SKIN STUFF. INLINE SKIN IS THE DEFAULT SKIN.
                     **/
                    Label inlineSkinTitle = new Label("Inline skin");
                    inlineSkinTitle.setFont(Font.font(24));

                    FXForm inlineForm = new FXFormBuilder<>()
                            .includeAndReorder("firstName", "lastName", "age", "favoriteMovie", "coolDeveloper")
                            .resourceBundle(Utils.SAMPLE)
                            .build();
                    User user = new User();
                    inlineForm.setSource(user);
                    box.getChildren().addAll(inlineSkinTitle, inlineForm);
                } else {


                    /**
                     ** INLINE SKIN STUFF. INLINE SKIN IS THE DEFAULT SKIN WHEN YOU USE FXFormBuilder.
                     **/
                    Label otherSkinTitle = new Label("Other skin");
                    otherSkinTitle.setFont(Font.font(24));

                    FXForm otherForm = new FXFormBuilder<>()
                            .includeAndReorder("firstName", "lastName", "age", "favoriteMovie", "coolDeveloper")
                            .resourceBundle(Utils.SAMPLE)
                            .build();
                    otherForm.setSkin(new DefaultSkin(otherForm));
                    User user2 = new User();
                    otherForm.setSource(user2);
                    box.getChildren().addAll(otherSkinTitle, otherForm);
                }
                bp.setCenter(box);
            }
        });
        choiceBox.getSelectionModel().selectFirst();
        return bp;
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
        return "This is an example to use different skin with your form";
    }
}
