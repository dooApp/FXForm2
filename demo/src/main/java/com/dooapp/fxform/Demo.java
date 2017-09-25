/*
 * Copyright (c) 2012, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform;

import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.view.FXFormSkin;
import com.dooapp.fxform.view.FXFormSkinFactory;
import com.dooapp.fxform.view.skin.FXMLSkin;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import javax.validation.ConstraintViolation;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 09/04/11
 * Time: 21:44
 * FXForm demo
 */
public class Demo extends Application {

    private FXForm<MyBean> fxForm;

    private StackPane root = new StackPane();

    private final String css = Demo.class.getResource("style.css").toExternalForm();

    private final String material = Demo.class.getResource("material-fx-v0.3.css").toExternalForm();

    protected void setup() {
        MyBean joe = new MyBean("Joe", "contact@", "How does this crazy form works?", true, MyBean.Subject.QUESTION);
        new ObjectPropertyObserver(joe);
        fxForm = new FXFormBuilder()
                .source(joe)
                .categorize("-USER-", "name", "welcome", "email", "-DATA-", "subject", "message")
                .resourceBundle(ResourceBundle.getBundle("com.dooapp.fxform.Demo"))
                .build();
        fxForm.setTitle("Dude, where is my form?");
        FXForm form2 = new FXFormBuilder<>().source(joe)
                .categorizeAndInclude("-USER-", "name", "welcome", "email", "-DATA-", "subject", "message")
                .resourceBundle(ResourceBundle.getBundle("com.dooapp.fxform.Demo"))
                .build();
        Scene scene = new Scene(form2);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        root.getChildren().add(createNode());
    }

    private Node createNode() {
        VBox vBox = new VBox();
        vBox.getChildren().addAll(createSkinSelector(), createCSSNode(), createSnapshotButton(), fxForm, createConstraintNode());
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private Button createSnapshotButton() {
        Button button = new Button("Snapshot");
        button.setOnAction(event -> {
            WritableImage writableImage = new WritableImage((int) fxForm.getWidth(), (int) fxForm.getHeight());
            fxForm.snapshot(null, writableImage);
            BufferedImage bImage = SwingFXUtils.fromFXImage(writableImage, null);
            try {
                ImageIO.write(bImage, "png", new File("fxform.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return button;
    }

    private Node createConstraintNode() {
        Button button = new Button("Validate");
        button.setDefaultButton(true);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ListView<ConstraintViolation> listView = new ListView<ConstraintViolation>();
                listView.setCellFactory(new Callback<ListView<ConstraintViolation>, ListCell<ConstraintViolation>>() {

                    @Override
                    public ListCell<ConstraintViolation> call(ListView<ConstraintViolation> constraintViolation) {
                        return new ListCell<ConstraintViolation>() {
                            @Override
                            protected void updateItem(ConstraintViolation constraintViolation, boolean b) {
                                super.updateItem(constraintViolation, b);
                                if (constraintViolation != null) {
                                    setText(constraintViolation.getPropertyPath().toString() + " - " + constraintViolation.getMessage());
                                } else {
                                    setText("");
                                }
                            }
                        };
                    }
                });
                listView.getItems().setAll(fxForm.getConstraintViolations());
                Scene scene = new Scene(listView);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
        });
        return button;
    }

    private Node createCSSNode() {
        CheckBox checkBox = new CheckBox("Use css");
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean1) {
                if (aBoolean1) {
                    root.getScene().getStylesheets().add(css);
                    root.getScene().getStylesheets().add(material);
                } else {
                    root.getScene().getStylesheets().remove(css);
                    root.getScene().getStylesheets().remove(material);
                }
            }
        });
        checkBox.setSelected(true);
        return checkBox;
    }

    /**
     * Create a selector that changes the skin of the form.
     *
     * @return
     */
    private Node createSkinSelector() {
        ChoiceBox<FXFormSkinFactory> choiceBox = new ChoiceBox<FXFormSkinFactory>();
        choiceBox.getItems().addAll(FXFormSkinFactory.DEFAULT_FACTORY, FXFormSkinFactory.INLINE_FACTORY, new FXFormSkinFactory() {
            @Override
            public FXFormSkin createSkin(FXForm form) {
                return new FXMLSkin(form, Demo.class.getResource("Demo_form.fxml"));
            }

            @Override
            public String toString() {
                return "FXML Skin (Demo_form.fxml)";
            }
        });
        choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FXFormSkinFactory>() {
            public void changed(ObservableValue<? extends FXFormSkinFactory> observableValue, FXFormSkinFactory fxFormSkinFactory, FXFormSkinFactory fxFormSkinFactory1) {
                fxForm.setSkin(fxFormSkinFactory1.createSkin(fxForm));
            }
        });
        choiceBox.getSelectionModel().selectFirst();
        return choiceBox;
    }

    public static void main(String[] args) {
        Application.launch(Demo.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("FXForm Demo");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(600);
        stage.setWidth(490);
        setup();
        stage.show();
    }

}
