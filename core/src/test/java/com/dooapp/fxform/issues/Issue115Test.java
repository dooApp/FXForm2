package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * Test designed to be run manually.
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 19/11/15
 * Time: 10:01
 */
public class Issue115Test extends Application {

    public static class TestBean {

        private StringProperty myProp = new SimpleStringProperty();

    }

    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        TitledPane titledPane = new TitledPane();
        FXForm fxForm = new FXForm();
        fxForm.setResourceBundle(ResourceBundle.getBundle("Issue115Test"));
        fxForm.setSource(new TestBean());
        titledPane.setContent(fxForm);
        Scene scene = new Scene(new StackPane(titledPane));
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
