package com.dooapp.fxform.samples;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.FXFormSample;
import com.dooapp.fxform.Utils;
import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.annotation.FormFactory;
import com.dooapp.fxform.builder.FXFormBuilder;
import com.dooapp.fxform.view.factory.DefaultFactoryProvider;
import com.dooapp.fxform.view.factory.impl.ListChoiceBoxFactory;
import com.dooapp.fxform.view.factory.impl.TextAreaFactory;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Bastien
 */
public class CustomFactoryForm extends FXFormSample {

    @Accessor(value = Accessor.AccessType.FIELD)
    public class UserWithCustomFactory {

        public StringProperty firstName = new SimpleStringProperty("Jon");

        @FormFactory(TextAreaFactory.class) //The field will use a TextArea instead of the default TextField
        public StringProperty lastName = new SimpleStringProperty();

        public IntegerProperty age = new SimpleIntegerProperty(10);

    }

    @Override
    public String getSampleName() {
        return "Custom factory form";
    }

    @Override
    public Node getPanel(Stage stage) {
        Pane root = new Pane();
        FXForm form = new FXFormBuilder<>().includeAndReorder("firstName", "lastName", "age").resourceBundle(Utils.SAMPLE).build();
        UserWithCustomFactory userWithCustomFactory = new UserWithCustomFactory();
        // another way to register a custom factory using the DefaultFactoryProvider
        DefaultFactoryProvider factoryProvider = new DefaultFactoryProvider();
        factoryProvider.addFactory(element -> true,
                new ListChoiceBoxFactory<>(new SimpleListProperty<>(FXCollections.observableArrayList("Robert", "Jon", "Catelyn"))));
        form.setEditorFactoryProvider(factoryProvider);
        form.setSource(userWithCustomFactory);
        root.getChildren().add(form);
        return root;
    }

    @Override
    public String getSampleDescription() {
        return "This is an example of how to use FXForm with a custom Factory. Here the default lastName TextField is replaced by a TextArea";
    }
}
