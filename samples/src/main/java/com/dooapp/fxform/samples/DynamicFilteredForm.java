package com.dooapp.fxform.samples;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.filter.ExcludeFilter;
import com.dooapp.fxform.sampler.FXFormSample;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Dynamic form sample provided by Oova https://github.com/oova
 *
 * Date: 14/12/2018
 * Time: 12:14
 */
public class DynamicFilteredForm extends FXFormSample {

    @Override
    public String getSampleName() {
        return "Dynamic filtered form";
    }

    @Accessor(value = Accessor.AccessType.METHOD)
    public static class MyBean {

        private BooleanProperty commentEnabled = new SimpleBooleanProperty(this, "commentEnabled", true);
        private IntegerProperty priority = new SimpleIntegerProperty(this, "priority", 1);
        private StringProperty comment = new SimpleStringProperty(this, "comment", "");

        public boolean isCommentEnabled() {
            return commentEnabled.get();
        }

        public BooleanProperty commentEnabledProperty() {
            return commentEnabled;
        }

        public int getPriority() {
            return priority.get();
        }

        public void setPriority(int priority) {
            this.priority.set(priority);
        }

        public IntegerProperty priorityProperty() {
            return priority;
        }

        @NotBlank
        public String getComment() {
            return comment.get();
        }

        public void setComment(String comment) {
            this.comment.set(comment);
        }

        public StringProperty commentProperty() {
            return comment;
        }
    }

    @Override
    public Node getPanel(Stage stage) {
        MyBean bean = new MyBean();
        FXForm<MyBean> form = new FXForm<>();
        form.setSource(bean);
        Pane root = new Pane(form);
        ExcludeFilter commentExclusion = new ExcludeFilter("comment");
        bean.commentEnabled.addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(() -> form.getFilters().remove(commentExclusion));
            } else {
                Platform.runLater(() -> form.addFilters(commentExclusion));
            }
        });
        return root;
    }

    @Override
    public String getControlStylesheetURL() {
        return null;
    }

}
