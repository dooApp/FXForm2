package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.FailOnUncaughtExceptionRule;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.TestUtils;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Issue167Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @Rule
    public FailOnUncaughtExceptionRule failOnUncaughtExceptionRule = new FailOnUncaughtExceptionRule();

    class TestBean {

        StringProperty name = new SimpleStringProperty();

        IntegerProperty age = new SimpleIntegerProperty();

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public int getAge() {
            return age.get();
        }

        public IntegerProperty ageProperty() {
            return age;
        }

        public void setAge(int age) {
            this.age.set(age);
        }

    }

    /**
     * When a bean property is updated in JavaFX Thread twice with the initial value, FXForm do not update its view with the last update.
     * Example : bean property value is name1
     * FXForm will show name1
     * Trigger bean property update to name2 and to name1 (initial value) in javafx thread
     * FXForm will show name2 instead of name1
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test167() throws ExecutionException, InterruptedException {
        TestBean testBean = new TestBean();
        testBean.setName("name1");
        FXForm fxForm = new FXForm(testBean);
        TestUtils.syncJavaFXThread();
        AtomicBoolean wait = new AtomicBoolean(true);
        testBean.nameProperty().addListener(new ChangeListener<String>() {
            boolean initialized = false;

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if ("name2".equals(newValue)) {
                    initialized = true;
                }
                if (initialized && "name1".equals(newValue)) {
                    wait.set(false);
                }
            }
        });
        Platform.runLater(() -> {
            testBean.setName("name2");
            testBean.setName("name1");
        });
        while (wait.get()) {
            Thread.sleep(100);
        }
        TestUtils.syncJavaFXThread();
        Assert.assertEquals("name1", ((TextField) fxForm.lookup("#name-form-editor")).getText());
    }

    /**
     * When a bean property is updated twice outside JavaFX thread with the initial value, FXForm do not update its view with the last update.
     * Example : bean property value is name1
     * FXForm will show name1
     * Trigger bean property update to name2 and to name1 (initial value) in another thread
     * Sometimes, FXForm will show name2 instead of name1
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test167_2() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 100; i++) {
            TestBean testBean = new TestBean();
            testBean.setName("name1");
            FXForm fxForm = new FXForm(testBean);
            TestUtils.syncJavaFXThread();
            AtomicBoolean wait = new AtomicBoolean(true);
            testBean.nameProperty().addListener(new ChangeListener<String>() {
                boolean initialized = false;

                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if ("name2".equals(newValue)) {
                        initialized = true;
                    }
                    if (initialized && "name1".equals(newValue)) {
                        wait.set(false);
                    }
                }
            });
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    testBean.setName("name2");
                    testBean.setName("name1");
                }
            });
            thread.start();
            while (wait.get()) {
                Thread.sleep(100);
            }
            TestUtils.syncJavaFXThread();
            Assert.assertEquals("name1", ((TextField) fxForm.lookup("#name-form-editor")).getText());
        }
    }
}
