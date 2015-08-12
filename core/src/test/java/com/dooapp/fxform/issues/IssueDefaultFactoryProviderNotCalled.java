package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.FXFormNodeWrapper;
import com.dooapp.fxform.view.factory.DefaultFactoryProvider;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by KEVIN on 12/08/2015.
 */
public class IssueDefaultFactoryProviderNotCalled extends Application {
    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @Test
    public void testUsingFormProvider() {
        FXForm form = new FXFormBuilder<>().build();

        DefaultFactoryProvider factoryProvider = (DefaultFactoryProvider) form.getEditorFactoryProvider();
        factoryProvider.addFactory(element -> SpecificationB.class.equals(element.getWrappedType()), new SpecificationBFactory());

        form.setEditorFactoryProvider(factoryProvider);

        Bean source = new Bean();
        source.setSpecification(new SpecificationB());

        form.setSource(source);
        Assert.assertNotNull(((Node) form.lookup("#specification-form-editor")).lookup("#test"));
    }

    @Test
    // should pass!
    public void testWithNewDefaultFactoryProvider() {
        FXForm form = new FXFormBuilder<>().build();

        DefaultFactoryProvider factoryProvider = new DefaultFactoryProvider();
        factoryProvider.addFactory(element -> SpecificationB.class.equals(element.getWrappedType()), new SpecificationBFactory());

        form.setEditorFactoryProvider(factoryProvider);

        Bean source = new Bean();
        source.setSpecification(new SpecificationB());

        form.setSource(source);
        Assert.assertNotNull(((Node) form.lookup("#specification-form-editor")).lookup("#test"));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox root = new HBox();
        FXForm form = new FXFormBuilder<>().build();
        DefaultFactoryProvider factoryProvider = (DefaultFactoryProvider) form.getEditorFactoryProvider();

        factoryProvider.addFactory(element -> SpecificationB.class.equals(element.getWrappedType()), new SpecificationBFactory());

        form.setEditorFactoryProvider(factoryProvider);

        Bean source = new Bean();
        source.setSpecification(new SpecificationB());

        form.setSource(source);

        root.getChildren().addAll(form);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class Bean {
        ObjectProperty<Specification> specification = new SimpleObjectProperty<>();

        public Specification getSpecification() {
            return specification.get();
        }

        public ObjectProperty<Specification> specificationProperty() {
            return specification;
        }

        public void setSpecification(Specification specification) {
            this.specification.set(specification);
        }
    }

    private interface Specification {
    }

    private class SpecificationB implements Specification {
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

    private class SpecificationBFactory implements Callback<Void, FXFormNode> {

        public FXFormNode call(Void param) {
            final Label label = new Label("TEST");
            label.setId("test");
            ObjectProperty property = new SimpleObjectProperty<>();
            return new FXFormNodeWrapper(label, property);
        }

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
