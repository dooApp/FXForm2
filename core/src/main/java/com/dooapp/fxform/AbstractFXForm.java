/*
 * Copyright (c) 2013, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform;

import com.dooapp.fxform.adapter.AdapterProvider;
import com.dooapp.fxform.adapter.DefaultAdapterProvider;
import com.dooapp.fxform.controller.ElementController;
import com.dooapp.fxform.controller.PropertyElementController;
import com.dooapp.fxform.filter.ElementListFilter;
import com.dooapp.fxform.filter.FilterException;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.PropertyElement;
import com.dooapp.fxform.resource.DefaultResourceProvider;
import com.dooapp.fxform.resource.ResourceProvider;
import com.dooapp.fxform.validation.ClassLevelValidator;
import com.dooapp.fxform.validation.DefaultFXFormValidator;
import com.dooapp.fxform.validation.FXFormValidator;
import com.dooapp.fxform.view.FXFormNode;
import com.dooapp.fxform.view.factory.DefaultFactoryProvider;
import com.dooapp.fxform.view.factory.DefaultLabelFactoryProvider;
import com.dooapp.fxform.view.factory.DefaultTooltipFactoryProvider;
import com.dooapp.fxform.view.factory.FactoryProvider;
import com.dooapp.fxform.view.factory.impl.DefaultConstraintFactory;
import com.dooapp.fxform.view.property.DefaultPropertyProvider;
import com.dooapp.fxform.view.property.PropertyProvider;
import com.dooapp.fxform.view.skin.DefaultSkin;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.util.Callback;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This control can be used to show a form to the user. The form handle a collection a Element.
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 03/12/2013
 * Time: 15:11
 */
public class AbstractFXForm extends Control {

    private final static Logger logger = Logger.getLogger(FXForm.class.getName());

    public final static String INVALID_STYLE = "-invalid";

    public static final String WARNING_STYLE = "-warning";

    private StringProperty title = new SimpleStringProperty();

    private final MapProperty<Element, ElementController> controllers = new SimpleMapProperty<Element, ElementController>(FXCollections.<Element, ElementController>observableHashMap());

    private final ListProperty<Element> elements = new SimpleListProperty<Element>(FXCollections.<Element>observableArrayList());

    private final ObjectProperty<ResourceBundle> resourceBundle = new SimpleObjectProperty<ResourceBundle>();

    private final ObjectProperty<FactoryProvider> editorFactoryProvider = new SimpleObjectProperty<FactoryProvider>();

    private final ObjectProperty<FactoryProvider> tooltipFactoryProvider = new SimpleObjectProperty<FactoryProvider>();

    private final ObjectProperty<FactoryProvider> labelFactoryProvider = new SimpleObjectProperty<FactoryProvider>();

    private final ObjectProperty<FactoryProvider> constraintFactoryProvider = new SimpleObjectProperty<FactoryProvider>();

    private final ObjectProperty<AdapterProvider> adapterProvider = new SimpleObjectProperty<AdapterProvider>();

    private final ObjectProperty<PropertyProvider> propertyProvider = new SimpleObjectProperty<PropertyProvider>();

    protected final ObservableList<ConstraintViolation> constraintViolationsList = FXCollections.<ConstraintViolation>observableArrayList();

    private final ObjectProperty<FXFormValidator> fxFormValidator = new SimpleObjectProperty<FXFormValidator>(new DefaultFXFormValidator());

    private final ClassLevelValidator classLevelValidator = new ClassLevelValidator();

    private final ResourceProvider resourceProvider = new DefaultResourceProvider();

    private final ListProperty<ElementListFilter> filters = new SimpleListProperty<ElementListFilter>(FXCollections.<ElementListFilter>observableArrayList());

    private final ListProperty<Element> filteredElements = new SimpleListProperty<Element>(FXCollections.<Element>observableArrayList());

    public void setTitle(String title) {
        this.title.set(title);
    }

    public AbstractFXForm() {
        resourceProvider.resourceBundleProperty().bind(resourceBundleProperty());
        setPropertyProvider(new DefaultPropertyProvider());
        setAdapterProvider(new DefaultAdapterProvider());
        setEditorFactoryProvider(new DefaultFactoryProvider());
        setLabelFactoryProvider(new DefaultLabelFactoryProvider());
        setTooltipFactoryProvider(new DefaultTooltipFactoryProvider());
        setConstraintFactoryProvider(new FactoryProvider() {
            public Callback<Void, FXFormNode> getFactory(Element element) {
                return new DefaultConstraintFactory();
            }
        });
        filters.addListener(new ChangeListener<ObservableList<ElementListFilter>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<ElementListFilter>> observableValue, ObservableList<ElementListFilter> elementListFilters, ObservableList<ElementListFilter> elementListFilters2) {
                filteredElements.setAll(filter(elements));
            }
        });
        elements.addListener(new ChangeListener<ObservableList<Element>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<Element>> observableValue, ObservableList<Element> oldElements, ObservableList<Element> newElements) {
                filteredElements.setAll(filter(elements));
            }
        });
        filteredElements.addListener(new ListChangeListener<Element>() {
            @Override
            public void onChanged(Change<? extends Element> change) {
                while (change.next()) {
                    configure(change.getAddedSubList());
                    unconfigure(change.getRemoved());
                }
            }
        });
        classLevelValidator.validatorProperty().bind(fxFormValidatorProperty());
        classLevelValidator.constraintViolationsProperty().addListener(new ListChangeListener<ConstraintViolation>() {
            @Override
            public void onChanged(Change<? extends ConstraintViolation> change) {
                while (change.next()) {
                    constraintViolationsList.removeAll(change.getRemoved());
                    constraintViolationsList.addAll(change.getAddedSubList());
                }
            }
        });
        this.setSkin(new DefaultSkin(this));
    }

    /**
     * Apply high level ElememtListFilter filters.
     *
     * @param elements
     * @return
     */
    protected List<Element> filter(ListProperty<Element> elements) {
        if (elements.size() == 0) {
            return elements;
        }
        List<Element> filteredList = new ArrayList<Element>(elements);
        for (ElementListFilter elementListFilter : filters) {
            try {
                filteredList = elementListFilter.filter(filteredList);
            } catch (FilterException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
        }
        return filteredList;
    }

    protected void unconfigure(List<? extends Element> removed) {
        for (Element element : removed) {
            if (controllers.containsKey(element)) {
                controllers.get(element).dispose();
                controllers.remove(element);
            }
        }
    }

    protected void configure(List<? extends Element> added) {
        for (Element element : added) {
            ElementController controller = null;
            if (PropertyElement.class.isAssignableFrom(element.getClass())) {
                controller = createPropertyElementController((PropertyElement) element);
            } else {
                controller = new ElementController(this, element);
            }
            controllers.put(element, controller);
        }
    }

    protected ElementController createPropertyElementController(PropertyElement element) {
        return new PropertyElementController(this, element);
    }

    public StringProperty titleProperty() {
        return title;
    }

    /**
     * Set the resource bundle used by this form to i18n labels, tooltips,...
     *
     * @param resourceBundle
     */
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle.set(resourceBundle);
    }

    public ObjectProperty<ResourceBundle> resourceBundleProperty() {
        return resourceBundle;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle.get();
    }

    public FactoryProvider getEditorFactoryProvider() {
        return editorFactoryProvider.get();
    }

    public FactoryProvider getTooltipFactoryProvider() {
        return tooltipFactoryProvider.get();
    }

    public FactoryProvider getLabelFactoryProvider() {
        return labelFactoryProvider.get();
    }

    public FactoryProvider getConstraintFactoryProvider() {
        return constraintFactoryProvider.get();
    }

    public void setEditorFactoryProvider(FactoryProvider editorFactoryProvider1) {
        editorFactoryProvider.set(editorFactoryProvider1);
    }

    public void setLabelFactoryProvider(FactoryProvider labelFactoryProvider1) {
        labelFactoryProvider.set(labelFactoryProvider1);
    }

    public void setTooltipFactoryProvider(FactoryProvider tooltipFactoryProvider1) {
        tooltipFactoryProvider.set(tooltipFactoryProvider1);
    }

    public void setConstraintFactoryProvider(FactoryProvider constraintFactoryProvider1) {
        constraintFactoryProvider.set(constraintFactoryProvider1);
    }

    public ObjectProperty<FactoryProvider> editorFactoryProvider() {
        return editorFactoryProvider;
    }

    public ObjectProperty<FactoryProvider> labelFactoryProvider() {
        return labelFactoryProvider;
    }

    public ObjectProperty<FactoryProvider> tooltipFactoryProvider() {
        return tooltipFactoryProvider;
    }

    public ObjectProperty<FactoryProvider> constraintFactoryProvider() {
        return constraintFactoryProvider;
    }

    public AdapterProvider getAdapterProvider() {
        return adapterProvider.get();
    }

    public void setAdapterProvider(AdapterProvider adapterProvider1) {
        this.adapterProvider.set(adapterProvider1);
    }

    public ObjectProperty<AdapterProvider> adapterProviderProperty() {
        return adapterProvider;
    }

    public PropertyProvider getPropertyProvider() {
        return propertyProvider.get();
    }

    public void setPropertyProvider(PropertyProvider propertyProvider) {
        this.propertyProvider.set(propertyProvider);
    }

    public ObjectProperty<PropertyProvider> propertyProviderProperty() {
        return propertyProvider;
    }

    public FXFormValidator getFxFormValidator() {
        return fxFormValidator.get();
    }

    public void setFxFormValidator(FXFormValidator fxFormValidator) {
        this.fxFormValidator.set(fxFormValidator);
    }

    public ObjectProperty<FXFormValidator> fxFormValidatorProperty() {
        return fxFormValidator;
    }

    /**
     * Get an ObservableList mirroring all constraint violations in the form.
     * This method can be used to implement some kind of validation of the form or
     * to display all constraint violations.
     * This list is updated each time the user inputs data that violates a constraint or fixes a violation.
     *
     * @return the ObservableList containing current constraint violations
     */
    public ObservableList<ConstraintViolation> getConstraintViolations() {
        return constraintViolationsList;
    }

    public ResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    public ObservableList<Element> getElements() {
        return elements.get();
    }

    public ListProperty<Element> elementsProperty() {
        return elements;
    }

    public void setElements(ListProperty<Element> elements) {
        this.elements.set(elements);
    }

    public ObservableList<Element> getFilteredElements() {
        return FXCollections.unmodifiableObservableList(filteredElements.get());
    }

    public ReadOnlyListProperty<Element> filteredElementsProperty() {
        return new ReadOnlyListWrapper<Element>(filteredElements);
    }

    public ClassLevelValidator getClassLevelValidator() {
        return classLevelValidator;
    }

    public ObservableList<ElementListFilter> getFilters() {
        return filters;
    }

    public void addFilters(ElementListFilter... filters) {
        this.filters.addAll(filters);
    }

}
