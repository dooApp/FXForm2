package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.adapter.Adapter;
import com.dooapp.fxform.adapter.AdapterException;
import com.dooapp.fxform.adapter.AdapterMatcher;
import com.dooapp.fxform.adapter.DefaultAdapterProvider;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 22/09/2017
 * Time: 11:20
 */
public class Issue127Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    public class TestBean {

        StringProperty name = new SimpleStringProperty();

        public String getName() {
            return name.get();
        }

        public StringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

    }

    @Test
    public void test127() throws ExecutionException, InterruptedException {
        final boolean[] failed = {false};
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            failed[0] = true;
        });
        TestBean testBean = new TestBean();
        FXForm fxForm = new FXForm(testBean);
        Thread thread1 = new Thread(() -> {
            while (true) {
                try {
                    testBean.setName("" + Math.random());
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                    failed[0] = true;
                }
            }
        }, "Thread 1");
        Thread thread2 = new Thread(() -> {
            while (true) {
                try {
                    ((DefaultAdapterProvider) fxForm.getAdapterProvider()).addAdapter(new AdapterMatcher() {
                        @Override
                        public boolean matches(Class fromClass, Class toClass, Element element, FXFormNode fxFormNode) {
                            return false;
                        }
                    }, new Adapter() {
                        @Override
                        public Object adaptTo(Object from) throws AdapterException {
                            return null;
                        }

                        @Override
                        public Object adaptFrom(Object to) throws AdapterException {
                            return null;
                        }
                    });
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                    failed[0] = true;
                }
            }
        }, "Thread 2");
        thread1.start();
        thread2.start();
        Thread.sleep(1000);
        Assert.assertFalse(failed[0]);
    }

}
