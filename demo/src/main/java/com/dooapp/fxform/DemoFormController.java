package com.dooapp.fxform;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 30/09/12
 * Time: 11:35
 */
public class DemoFormController implements Initializable {

    @FXML
    ChoiceBox subjectFormEditor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        subjectFormEditor.getItems().setAll(MyBean.Subject.values());
    }

}
