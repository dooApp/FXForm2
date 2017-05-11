package com.dooapp.fxform.issues.issue142;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.view.property.DefaultPropertyProvider;
import com.dooapp.fxform.view.skin.FXMLSkin;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by KEVIN on 04/05/2017.
 */
public class Issue142Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    class Service extends javafx.concurrent.Service<Void> {

        private final Issue142Bean bean1;
        private final Issue142Bean bean2;
        private final FXForm fxForm;

        public Service(Issue142Bean bean1, Issue142Bean bean2, FXForm fxForm) {
            this.bean1 = bean1;
            this.bean2 = bean2;
            this.fxForm = fxForm;
        }

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(50);
                        Issue142Bean p = i % 2 == 0 ? bean1 : bean2;
                        Platform.runLater(() -> {
                            fxForm.setSource(null);
                            fxForm.setSource(p);
                        });
                    }
                    return null;
                }
            };
        }
    }


    public static void main(String[] args) {
        Application.launch(args);
    }

    @Test
    public void test() {
        Issue142Bean bean1 = new Issue142Bean();
        bean1.getValues().add("a");
        Issue142Bean bean2 = new Issue142Bean();

        DefaultPropertyProvider.addGlobalProvider(Issue142CustomControl.class, customControl -> ((Issue142CustomControl) customControl).valuesProperty());

        FXForm fxForm = new FXForm();
        fxForm.setSkin(new FXMLSkin(fxForm, Issue142Test.class.getClassLoader().getResource("issues/issue142/issue142.fxml")));

        final boolean[] finished = {false};

        Service service = new Service(bean1, bean2, fxForm);
        service.stateProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case SUCCEEDED:
                case CANCELLED:
                case FAILED:
                    finished[0] = true;
                    break;
            }
        });
        service.restart();
        while (!finished[0]) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
        Assert.assertFalse(bean1.getValues().isEmpty());
        Assert.assertTrue(bean2.getValues().isEmpty());
    }
}
