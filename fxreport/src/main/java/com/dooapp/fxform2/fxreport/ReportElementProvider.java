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
import java.util.*;

public class ReportElementProvider implements ElementProvider {

    final Map<String, Property> properties = new LinkedHashMap<>();
    final Map<String, List<String>> listFields = new HashMap<>();

    public ReportElementProvider(IXDocReport report) throws IOException, XDocReportException {
        FieldsExtractor extractor = new FieldsExtractor();
        report.extractFields(extractor);
        List<FieldExtractor> fields = extractor.getFields();
        fields.stream().filter(field -> !field.getName().contains("___") && field.getName().startsWith("_"))
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
                    if (!field.isList()) {
                        properties.put(field.getName(),
                                new SimpleStringProperty(null, splitted[0]));
                    } else {
                        String[] loopSplit = splitted[0].split("\\.");
                        String loopFieldName = loopSplit[0];
                        if (!properties.containsKey(loopFieldName)) {
                            ListProperty listProperty = new SimpleListProperty(null, loopFieldName);
                            listProperty.setValue(FXCollections.observableArrayList());
                            properties.put(loopFieldName, listProperty);
                            listFields.put(loopFieldName, new LinkedList<>());
                        }
                        listFields.get(loopFieldName).add(loopSplit[1]);
                    }
                });
    }

    public void fillContext(IContext context) {
        properties.keySet().forEach(key -> context.put(key, properties.get(key).getValue()));
    }

    @Override
    public <T> ListProperty<Element> getElements(ObjectProperty<T> source) {
        SimpleListProperty<Element> elements = new SimpleListProperty<>(FXCollections.observableArrayList());
        properties.entrySet().forEach(entry -> {
            if (entry.getValue() instanceof ListProperty) {
                elements.add(new ListElementWrapper(entry.getValue(), listFields.get(entry.getKey())));
            } else {
                elements.add(new PropertyElementWrapper(entry.getValue(), String.class));
            }
        });
        return elements;
    }

}
