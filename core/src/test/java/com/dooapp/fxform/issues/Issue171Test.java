package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.adapter.Adapter;
import com.dooapp.fxform.adapter.AdapterException;
import com.dooapp.fxform.adapter.DefaultAdapterProvider;
import com.dooapp.fxform.adapter.ObjectPropertyAdapterMatcher;
import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.handler.WrappedTypeHandler;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.factory.DefaultFactoryProvider;
import com.dooapp.fxform.view.factory.impl.TextFieldFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 16/11/2018
 * Time: 17:46
 */
public class Issue171Test {

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @Test
    public void test() {
        FXForm<User> form = new FXForm<>();

        User user = new User();
        Partner partner = new Partner();
        user.partner.set(partner);

        DefaultFactoryProvider editorFactoryProvider = new DefaultFactoryProvider();
        editorFactoryProvider.addFactory(new WrappedTypeHandler(Partner.class), new TextFieldFactory());
        form.setEditorFactoryProvider(editorFactoryProvider);

        DefaultAdapterProvider adapterProvider = new DefaultAdapterProvider();
        adapterProvider.addAdapter(new ObjectPropertyAdapterMatcher(Partner.class, StringProperty.class), new PartnerAdapter());
        form.setAdapterProvider(adapterProvider);

        form.setSource(user);
        Element partnerElement = form.getElements().stream().filter(element -> "partner".equals(element.getName())).findFirst().get();
        Assert.assertTrue(form.getFxFormValidator().validate(partnerElement, partnerElement.getValue()).isEmpty());
        partner.setFirstName("");
        Assert.assertFalse(form.getFxFormValidator().validate(partnerElement, partnerElement.getValue()).isEmpty());
    }

    @Accessor(value = Accessor.AccessType.METHOD)
    public static class User {
        public StringProperty firstName = new SimpleStringProperty("Jon");
        public StringProperty lastName = new SimpleStringProperty("Smith");
        public ObjectProperty<Partner> partner = new SimpleObjectProperty<>(this, "partner");

        @NotBlank
        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String firstName) {
            this.firstName.set(firstName);
        }

        public StringProperty firstNameProperty() {
            return firstName;
        }

        public String getLastName() {
            return lastName.get();
        }

        public void setLastName(String lastName) {
            this.lastName.set(lastName);
        }

        public StringProperty lastNameProperty() {
            return lastName;
        }

        @NotNull
        @Valid
        public Partner getPartner() {
            return partner.get();
        }

        public void setPartner(Partner partner) {
            this.partner.set(partner);
        }

        public ObjectProperty<Partner> partnerProperty() {
            return partner;
        }
    }

    @Accessor(value = Accessor.AccessType.METHOD)
    public static class Partner {
        public StringProperty firstName = new SimpleStringProperty("Jack");
        public StringProperty lastName = new SimpleStringProperty("Myers");

        @NotBlank
        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String firstName) {
            this.firstName.set(firstName);
        }

        public StringProperty firstNameProperty() {
            return firstName;
        }

        public String getLastName() {
            return lastName.get();
        }

        public void setLastName(String lastName) {
            this.lastName.set(lastName);
        }

        public StringProperty lastNameProperty() {
            return lastName;
        }

        @Override
        public String toString() {
            return getFirstName() + " " + getLastName();
        }
    }

    public static class PartnerAdapter implements Adapter<Partner, String> {

        @Override
        public String adaptTo(Partner from) throws AdapterException {
            if (from == null) {
                return null;
            }
            return from.getFirstName();
        }

        @Override
        public Partner adaptFrom(String to) throws AdapterException {
            Partner p = new Partner();
            p.setFirstName(to);
            return p;
        }

    }

}

