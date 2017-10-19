package com.dooapp.fxform.issues.issue154;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.TestUtils;
import com.dooapp.fxform.view.property.DefaultPropertyProvider;
import com.dooapp.fxform.view.property.PropertyProvider;
import com.dooapp.fxform.view.skin.FXMLSkin;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
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
        private ListProperty<Double> listItems = new SimpleListProperty(FXCollections.observableArrayList());
        private MapProperty<String, Double> mapItems = new SimpleMapProperty<>(FXCollections.observableHashMap());

        public ObservableList<Double> getListItems() {
            return listItems.get();
        }

        public ListProperty<Double> listItemsProperty() {
            return listItems;
        }

        public void setListItems(ObservableList<Double> listItems) {
            this.listItems.set(listItems);
        }

        public ObservableMap<String, Double> getMapItems() {
            return mapItems.get();
        }

        public MapProperty<String, Double> mapItemsProperty() {
            return mapItems;
        }

        public void setMapItems(ObservableMap<String, Double> mapItems) {
            this.mapItems.set(mapItems);
        }
    }

    @Test
    public void testToSetTheSourceWithBeansContainingListProperty() throws InterruptedException, ExecutionException {
        DefaultPropertyProvider.addGlobalProvider(Issue154ControlList.class, new PropertyProvider<Issue154ControlList>() {
            @Override
            public Property getProperty(Issue154ControlList control) {
                return control.itemsProperty();
            }
        });

        FXForm fxForm = new FXForm();
        fxForm.setSkin(new FXMLSkin(fxForm, Issue154Test.class.getClassLoader().getResource("issues/issue154/issue154.fxml")));

        TestBean testBean = new TestBean();
        testBean.getListItems().add(10d);
        fxForm.setSource(testBean);
        TestUtils.syncJavaFXThread();

        TestBean testBean2 = new TestBean();
        fxForm.setSource(testBean2);
        TestUtils.syncJavaFXThread();

        TestBean testBean3 = new TestBean();
        fxForm.setSource(testBean3);
        TestUtils.syncJavaFXThread();

        Issue154ControlList customControl = (Issue154ControlList) fxForm.lookup("#listItems-form-editor");
        customControl.getItems().add(20d);
        TestUtils.syncJavaFXThread();

        Assert.assertEquals(1, testBean.getListItems().size());
        Assert.assertEquals(10d, testBean.getListItems().get(0).doubleValue(), 0.01);

        Assert.assertEquals(0, testBean2.getListItems().size());

        Assert.assertEquals(1, testBean3.getListItems().size());
        Assert.assertEquals(20d, testBean3.getListItems().get(0).doubleValue(), 0.01);
    }

    @Test
    public void testToSetTheSourceWithBeansContainingMapProperty() throws InterruptedException, ExecutionException {
        DefaultPropertyProvider.addGlobalProvider(Issue154ControlMap.class, new PropertyProvider<Issue154ControlMap>() {
            @Override
            public Property getProperty(Issue154ControlMap control) {
                return control.mapItemsProperty();
            }
        });

        FXForm fxForm = new FXForm();
        fxForm.setSkin(new FXMLSkin(fxForm, Issue154Test.class.getClassLoader().getResource("issues/issue154/issue154.fxml")));

        TestBean testBean = new TestBean();
        testBean.getMapItems().put("John", 17d);
        fxForm.setSource(testBean);
        TestUtils.syncJavaFXThread();

        TestBean testBean2 = new TestBean();
        fxForm.setSource(testBean2);
        TestUtils.syncJavaFXThread();

        TestBean testBean3 = new TestBean();
        fxForm.setSource(testBean3);
        TestUtils.syncJavaFXThread();

        Issue154ControlMap customControl = (Issue154ControlMap) fxForm.lookup("#mapItems-form-editor");
        customControl.getMapItems().put("Daenerys", 16d);
        TestUtils.syncJavaFXThread();

        Assert.assertEquals(1, testBean.getMapItems().size());
        Assert.assertEquals(17d, testBean.getMapItems().get("John").doubleValue(), 0.01);

        Assert.assertEquals(0, testBean2.getMapItems().size());

        Assert.assertEquals(1, testBean3.getMapItems().size());
        Assert.assertEquals(16d, testBean3.getMapItems().get("Daenerys").doubleValue(), 0.01);
    }
}


