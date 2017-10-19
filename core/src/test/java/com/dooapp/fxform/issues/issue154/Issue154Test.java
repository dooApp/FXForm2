package com.dooapp.fxform.issues.issue154;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.TestUtils;
import com.dooapp.fxform.view.property.DefaultPropertyProvider;
import com.dooapp.fxform.view.property.PropertyProvider;
import com.dooapp.fxform.view.skin.FXMLSkin;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 15/05/2017
 * Time: 17:12
 */
public class Issue154Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();


    class TestBean {
        private ListProperty<Double> items = new SimpleListProperty(FXCollections.observableArrayList());

        public ObservableList<Double> getItems() {
            return items.get();
        }

        public ListProperty<Double> itemsProperty() {
            return items;
        }

        public void setItems(ObservableList<Double> items) {
            this.items.set(items);
        }
    }

    @Test
    public void testToSetTheSourceWithBeansContainingListProperty() throws InterruptedException, ExecutionException {
        DefaultPropertyProvider.addGlobalProvider(Issue154CustomControl.class, new PropertyProvider<Issue154CustomControl>() {
            @Override
            public Property getProperty(Issue154CustomControl control) {
                return control.itemsProperty();
            }
        });

        FXForm fxForm = new FXForm();
        fxForm.setSkin(new FXMLSkin(fxForm, Issue154Test.class.getClassLoader().getResource("issues/issue154/issue154.fxml")));

        TestBean picturedDiagnosis = new TestBean();
        picturedDiagnosis.getItems().add(10d);
        fxForm.setSource(picturedDiagnosis);
        TestUtils.syncJavaFXThread();

        TestBean picturedDiagnosis2 = new TestBean();
        fxForm.setSource(picturedDiagnosis2);
        TestUtils.syncJavaFXThread();

        TestBean picturedDiagnosis3 = new TestBean();
        fxForm.setSource(picturedDiagnosis3);
        TestUtils.syncJavaFXThread();

        Issue154CustomControl flowDisplayer = (Issue154CustomControl) fxForm.lookup("#items-form-editor");
        flowDisplayer.getItems().add(20d);
        TestUtils.syncJavaFXThread();

        Assert.assertEquals(1, picturedDiagnosis.getItems().size());
        Assert.assertEquals(10d, picturedDiagnosis.getItems().get(0).doubleValue(), 0.01);

        Assert.assertEquals(0, picturedDiagnosis2.getItems().size());

        Assert.assertEquals(1, picturedDiagnosis3.getItems().size());
        Assert.assertEquals(20d, picturedDiagnosis3.getItems().get(0).doubleValue(), 0.01);
    }
}


