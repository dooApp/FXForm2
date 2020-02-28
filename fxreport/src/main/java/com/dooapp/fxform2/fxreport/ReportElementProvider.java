package com.dooapp.fxform2.fxreport;

import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.ElementProvider;
import com.dooapp.fxform.model.impl.PropertyElementWrapper;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.factory.DefaultFactoryProvider;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.template.FieldExtractor;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.IContext;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.util.Callback;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportElementProvider implements ElementProvider {

    final Map<String, Property> properties = new LinkedHashMap<>();

    public ReportElementProvider(IXDocReport report) throws IOException, XDocReportException {
        FieldsExtractor extractor = new FieldsExtractor();
        report.extractFields(extractor);
        List<FieldExtractor> fields = extractor.getFields();
        fields.stream().filter(field -> !field.getName().contains("___"))
                .forEach(field -> {
                    String[] splitted = field.getName().split("_");
                    if (splitted.length > 1) {
                        DefaultFactoryProvider.addGlobalFactory(element -> splitted[0].equals(element.getName()),
                                new Callback<Void, FXFormNode>() {
                                    @Override
                                    public FXFormNode call(Void param) {
                                        try {
                                            return ((Callback<Void, FXFormNode>) this.getClass().getClassLoader().loadClass(splitted[1]).newInstance()).call(param);
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        } catch (InstantiationException e) {
                                            e.printStackTrace();
                                        }
                                        return null;
                                    }
                                });
                    }
                    properties.put(splitted[0],
                            new SimpleStringProperty(null, splitted[0]));
                });
    }

    public void fillContext(IContext context) {
        properties.keySet().forEach(key -> context.put(key, properties.get(key).getValue()));
    }

    @Override
    public <T> ListProperty<Element> getElements(ObjectProperty<T> source) {
        SimpleListProperty<Element> elements = new SimpleListProperty<>(FXCollections.observableArrayList());
        properties.keySet().forEach(key ->
                elements.add(new PropertyElementWrapper(properties.get(key), String.class)));
        return elements;
    }

}
