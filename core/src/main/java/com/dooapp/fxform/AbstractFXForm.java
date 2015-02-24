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
import javafx.scene.control.Skin;
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

    public final static String INVALID_STYLE = "-invalid";

    public static final String WARNING_STYLE = "-warning";

    private final static Logger logger = Logger.getLogger(FXForm.class.getName());

    protected final ObservableList<ConstraintViolation> constraintViolationsList = FXCollections.<ConstraintViolation>observableArrayList();

    private final MapProperty<Element, ElementController> controllers = new SimpleMapProperty<Element, ElementController>(FXCollections.<Element, ElementController>observableHashMap());

    private final ListProperty<Element> elements = new SimpleListProperty<Element>(FXCollections.<Element>observableArrayList());

    private final ObjectProperty<ResourceBundle> resourceBundle = new SimpleObjectProperty<ResourceBundle>();

    private ClassLevelValidator classLevelValidator = null;

    private final ResourceProvider resourceProvider = new DefaultResourceProvider();

    private final ListProperty<ElementListFilter> filters = new SimpleListProperty<ElementListFilter>(FXCollections.<ElementListFilter>observableArrayList());

    private final ListProperty<Element> filteredElements = new SimpleListProperty<Element>(FXCollections.<Element>observableArrayList());

    private StringProperty title = new SimpleStringProperty();

    private ObjectProperty<FactoryProvider> editorFactoryProvider;

    private ObjectProperty<FactoryProvider> tooltipFactoryProvider;

    private ObjectProperty<FactoryProvider> labelFactoryProvider;

    private ObjectProperty<FactoryProvider> constraintFactoryProvider;

    private ObjectProperty<AdapterProvider> adapterProvider;

    private ObjectProperty<PropertyProvider> propertyProvider;

    private ObjectProperty<FXFormValidator> fxFormValidator;

    public AbstractFXForm() {
        resourceProvider.resourceBundleProperty().bind(resourceBundleProperty());
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
                    unconfigure(change.getRemoved());
                    configure(change.getAddedSubList());
                }
            }
        });
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DefaultSkin(this);
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

    public ObjectProperty<ResourceBundle> resourceBundleProperty() {
        return resourceBundle;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle.get();
    }

    /**
     * Set the resource bundle used by this form to i18n labels, tooltips,...
     *
     * @param resourceBundle
     */
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle.set(resourceBundle);
    }

    public FactoryProvider getEditorFactoryProvider() {
        return editorFactoryProviderProperty().get();
    }

    public void setEditorFactoryProvider(FactoryProvider editorFactoryProvider1) {
        editorFactoryProviderProperty().set(editorFactoryProvider1);
    }

    public FactoryProvider getTooltipFactoryProvider() {
        return tooltipFactoryProviderProperty().get();
    }

    public void setTooltipFactoryProvider(FactoryProvider tooltipFactoryProvider1) {
        tooltipFactoryProviderProperty().set(tooltipFactoryProvider1);
    }

    public FactoryProvider getLabelFactoryProvider() {
        return labelFactoryProviderProperty().get();
    }

    public void setLabelFactoryProvider(FactoryProvider labelFactoryProvider1) {
        labelFactoryProviderProperty().set(labelFactoryProvider1);
    }

    public FactoryProvider getConstraintFactoryProvider() {
        return constraintFactoryProviderProperty().get();
    }

    public void setConstraintFactoryProvider(FactoryProvider constraintFactoryProvider1) {
        constraintFactoryProviderProperty().set(constraintFactoryProvider1);
    }

    public ObjectProperty<FactoryProvider> constraintFactoryProviderProperty() {
        if (constraintFactoryProvider == null) {
            constraintFactoryProvider = createConstraintFactoryProvider();
        }
        return constraintFactoryProvider;
    }

    protected ObjectProperty<FactoryProvider> createConstraintFactoryProvider() {
        return new SimpleObjectProperty<>(new FactoryProvider() {
            public Callback<Void, FXFormNode> getFactory(Element element) {
                return new DefaultConstraintFactory();
            }
        });
    }

    public ObjectProperty<FactoryProvider> editorFactoryProviderProperty() {
        if (editorFactoryProvider == null) {
            editorFactoryProvider = createEditorFactoryProviderProperty();
        }
        return editorFactoryProvider;
    }

    protected ObjectProperty<FactoryProvider> createEditorFactoryProviderProperty() {
        return new SimpleObjectProperty<>(new DefaultFactoryProvider());
    }

    public ObjectProperty<FactoryProvider> labelFactoryProviderProperty() {
        if (labelFactoryProvider == null) {
            labelFactoryProvider = createLabelFactoryProvider();
        }
        return labelFactoryProvider;
    }

    protected ObjectProperty<FactoryProvider> createLabelFactoryProvider() {
        return new SimpleObjectProperty<>(new DefaultLabelFactoryProvider());
    }

    public ObjectProperty<FactoryProvider> tooltipFactoryProviderProperty() {
        if (tooltipFactoryProvider == null) {
            tooltipFactoryProvider = createTooltipFactoryProviderProperty();
        }
        return tooltipFactoryProvider;
    }

    protected ObjectProperty<FactoryProvider> createTooltipFactoryProviderProperty() {
        return new SimpleObjectProperty<>(new DefaultTooltipFactoryProvider());
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
        return adapterProviderProperty().get();
    }

    public void setAdapterProvider(AdapterProvider adapterProvider1) {
        this.adapterProviderProperty().set(adapterProvider1);
    }

    public ObjectProperty<AdapterProvider> adapterProviderProperty() {
        if (adapterProvider == null) {
            adapterProvider = createAdapterProviderProperty();
        }
        return adapterProvider;
    }

    protected ObjectProperty<AdapterProvider> createAdapterProviderProperty() {
        return new SimpleObjectProperty<>(new DefaultAdapterProvider());
    }

    public PropertyProvider getPropertyProvider() {
        return propertyProviderProperty().get();
    }

    public void setPropertyProvider(PropertyProvider propertyProvider) {
        this.propertyProviderProperty().set(propertyProvider);
    }

    public ObjectProperty<PropertyProvider> propertyProviderProperty() {
        if (propertyProvider == null) {
            propertyProvider = createPropertyProviderProperty();
        }
        return propertyProvider;
    }

    protected ObjectProperty<PropertyProvider> createPropertyProviderProperty() {
        return new SimpleObjectProperty<>(new DefaultPropertyProvider());
    }

    public FXFormValidator getFxFormValidator() {
        return fxFormValidatorProperty().get();
    }

    public void setFxFormValidator(FXFormValidator fxFormValidator) {
        this.fxFormValidatorProperty().set(fxFormValidator);
    }

    public ObjectProperty<FXFormValidator> fxFormValidatorProperty() {
        if (fxFormValidator == null) {
            fxFormValidator = createFxFormValidatorProperty();
        }
        return fxFormValidator;
    }

    protected SimpleObjectProperty<FXFormValidator> createFxFormValidatorProperty() {
        return new SimpleObjectProperty<>(new DefaultFXFormValidator());
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

    public void setElements(ListProperty<Element> elements) {
        this.elements.set(elements);
    }

    public ListProperty<Element> elementsProperty() {
        return elements;
    }

    public ObservableList<Element> getFilteredElements() {
        return FXCollections.unmodifiableObservableList(filteredElements.get());
    }

    public ReadOnlyListProperty<Element> filteredElementsProperty() {
        return new ReadOnlyListWrapper<Element>(filteredElements);
    }

    public ClassLevelValidator getClassLevelValidator() {
        if (classLevelValidator == null) {
            classLevelValidator = createClassLevelValidator();
        }
        return classLevelValidator;
    }

    protected ClassLevelValidator createClassLevelValidator() {
        ClassLevelValidator clv = new ClassLevelValidator(this);
        clv.validatorProperty().bind(fxFormValidatorProperty());
        clv.constraintViolationsProperty().addListener(new ListChangeListener<ConstraintViolation>() {
            @Override
            public void onChanged(Change<? extends ConstraintViolation> change) {
                while (change.next()) {
                    constraintViolationsList.removeAll(change.getRemoved());
                    constraintViolationsList.addAll(change.getAddedSubList());
                }
            }
        });
        return clv;
    }

    public ObservableList<ElementListFilter> getFilters() {
        return filters;
    }

    public void addFilters(ElementListFilter... filters) {
        this.filters.addAll(filters);
    }

    public ElementController getController(Element element) {
        return controllers.get(element);
    }

}
