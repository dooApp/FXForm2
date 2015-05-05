package com.dooapp.fxform.issues;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.JavaFXRule;
import com.dooapp.fxform.adapter.Adapter;
import com.dooapp.fxform.adapter.FormAdapter;
import com.dooapp.fxform.annotation.FormFactory;
import com.dooapp.fxform.view.factory.impl.DatePickerFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 05/05/15
 * Time: 15:47
 */
public class Issue101Test {

    private class TestBean {

        @FormAdapter(LocalDateAdatper.class)
        @FormFactory(DatePickerFactory.class)
        private ObjectProperty<Date> date = new SimpleObjectProperty<>(new Date());

    }

    public static class LocalDateAdatper implements Adapter<Date, LocalDate> {
        Calendar c = new GregorianCalendar(1800, 01, 01);

        @Override
        public LocalDate adaptTo(Date date) {
            if (date != null) {
                if (!date.before(c.getTime())) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    return LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                }
            }
            return null;
        }

        @Override
        public Date adaptFrom(LocalDate localDate) {
            if (localDate != null) {
                Instant instant = localDate.atTime(12, 0).atZone(ZoneId.systemDefault()).toInstant();
                return Date.from(instant);
            }
            return null;
        }
    }

    @Rule
    public JavaFXRule javaFXRule = new JavaFXRule();

    @Test
    public void test() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Assert.fail());
        FXForm fxForm = new FXForm();
        fxForm.setSource(new TestBean());

    }

}
