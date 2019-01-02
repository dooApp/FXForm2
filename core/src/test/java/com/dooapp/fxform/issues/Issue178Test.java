package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.annotation.Accessor;
import com.dooapp.fxform.filter.ExcludeFilter;
import javafx.beans.property.*;
import org.hibernate.validator.constraints.NotBlank;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 02/01/2019
 * Time: 17:20
 */
public class Issue178Test {

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

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @Test
    public void test() {
        MyBean bean = new MyBean();
        FXForm<MyBean> form = new FXForm<>();
        form.setSource(bean);
        Assert.assertEquals(1, form.getConstraintViolations().size());
        Assert.assertEquals("comment", form.getConstraintViolations().get(0).getPropertyPath().iterator().next().getName());
        ExcludeFilter commentExclusion = new ExcludeFilter("comment");
        form.getFilters().setAll(commentExclusion);
        Assert.assertEquals(0, form.getConstraintViolations().size());
        form.getFilters().clear();
        Assert.assertEquals(1, form.getConstraintViolations().size());
        Assert.assertEquals("comment", form.getConstraintViolations().get(0).getPropertyPath().iterator().next().getName());
    }

}
