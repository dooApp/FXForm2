package com.dooapp.fxform;

import com.dooapp.fxform.annotation.FormFactory;
import javafx.beans.property.*;
import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created at 26/09/12 14:23.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class MyBean {

    public static enum Subject {
        CONTACT, QUESTION, BUG, FEEDBACK
    }

    private final StringProperty name = new SimpleStringProperty();

    private final StringProperty email = new SimpleStringProperty();

    @FormFactory(Demo.TextAreaFactory.class)
    private final StringProperty message = new SimpleStringProperty();

    private final StringProperty website = new SimpleStringProperty();

    private final BooleanProperty subscribe = new SimpleBooleanProperty();

    private final ObjectProperty<Subject> subject = new SimpleObjectProperty<Subject>();

    private final DoubleProperty age = new SimpleDoubleProperty();

    protected MyBean(String name, String email, String message, String website, boolean subscribe, Subject subject) {
        this.name.set(name);
        this.email.set(email);
        this.message.set(message);
        this.website.set(website);
        this.subscribe.set(subscribe);
        this.subject.set(subject);
    }

    @Email
    public String getEmail() {
        return email.get();
    }

}