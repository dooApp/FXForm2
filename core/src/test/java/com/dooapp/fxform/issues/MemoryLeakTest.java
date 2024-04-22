package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.lang.ref.WeakReference;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 15/01/2020
 * Time: 15:54
 */
public class MemoryLeakTest {

    class TestBean {

        StringProperty stringProperty = new SimpleStringProperty();

        ListProperty listProperty = new SimpleListProperty(FXCollections.observableArrayList());

        MapProperty mapProperty = new SimpleMapProperty(FXCollections.observableHashMap());

        public String getStringProperty() {
            return stringProperty.get();
        }

        public StringProperty stringPropertyProperty() {
            return stringProperty;
        }

        public void setStringProperty(String stringProperty) {
            this.stringProperty.set(stringProperty);
        }

        public Object getListProperty() {
            return listProperty.get();
        }

        public ListProperty listPropertyProperty() {
            return listProperty;
        }

        public void setListProperty(Object listProperty) {
            this.listProperty.set(listProperty);
        }

        public Object getMapProperty() {
            return mapProperty.get();
        }

        public MapProperty mapPropertyProperty() {
            return mapProperty;
        }

        public void setMapProperty(Object mapProperty) {
            this.mapProperty.set(mapProperty);
        }

    }

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    FXForm fxForm;

    WeakReference<FXForm> fxFormWeakReference;

    WeakReference<Node> nodeWeakReference;

    TestBean testBean;

    @Test
    public void testMemoryLeak() throws InterruptedException {
        testBean = new TestBean();
        fxForm = new FXForm();
        fxFormWeakReference = new WeakReference<>(fxForm);
        fxForm.setSource(testBean);
        nodeWeakReference = new WeakReference(fxForm
                .getController(fxForm.getElements().get(0))
                .getEditorController().getNode());
        Assert.assertNotNull(fxFormWeakReference.get());
        Assert.assertNotNull(nodeWeakReference.get());
        fxForm = null;
        forceGc(fxFormWeakReference, 1000);
        Assert.assertTrue(fxFormWeakReference.refersTo(null));
        Assert.assertNull(nodeWeakReference.get());
    }

    private void forceGc(WeakReference fxFormWeakReference, int timeout) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        while (fxFormWeakReference.get() != null) {
            System.gc();
            Thread.sleep(100);
            if (System.currentTimeMillis() - startTime > timeout) break;
        }
    }

}
