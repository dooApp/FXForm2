package com.dooapp.fxform.controller;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.PropertyElement;
import com.dooapp.fxform.view.FXFormNode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

import javax.validation.*;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created at 27/09/12 17:32.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class PropertyEditorController extends NodeController {
    /**
     * The logger
     */
    private static final Logger logger = Logger.getLogger(PropertyEditorController.class.getName());

    private final ObservableList<ConstraintViolation> constraintViolations;

    static ValidatorFactory factory;

    Validator validator;
    private ChangeListener viewChangeListener;
    private ChangeListener modelChangeListener;

    /**
     * Initialize the constraint validator. Might be null after that if no implementation has been provided.
     */
    private void createValidator() {
        try {
            if (factory == null) {
                factory = Validation.buildDefaultValidatorFactory();
            }
            validator = factory.getValidator();
        } catch (ValidationException e) {
            // validation is not activated, since no implementation has been provided
            logger.log(Level.INFO, "Validation disabled", e);
        }
    }

    public PropertyEditorController(FXForm fxForm, Element element, ObservableList<ConstraintViolation> constraintViolations) {
        super(fxForm, element);
        createValidator();
        this.constraintViolations = constraintViolations;
    }

    @Override
    protected void bind(final FXFormNode fxFormNode) {
        viewChangeListener = new ChangeListener() {
            public void changed(ObservableValue observableValue, Object o, Object o1) {
                Object newValue = getFxForm().getAdapterProvider().getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode()).adaptFrom(o1);
                if (validator != null) {
                    Set<ConstraintViolation<Object>> constraintViolationSet = validator.validateValue((Class<Object>) (getElement().getBean().getClass()), getElement().getName(), newValue);
                    constraintViolations.clear();
                    constraintViolations.addAll(constraintViolationSet);
                }
                if (constraintViolations.size() == 0) {
                    ((PropertyElement) getElement()).setValue(newValue);
                }
            }
        };
        fxFormNode.getProperty().addListener(viewChangeListener);
        modelChangeListener = new ChangeListener() {
            public void changed(ObservableValue observableValue, Object o, Object o1) {
                Object newValue = getFxForm().getAdapterProvider().getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode()).adaptTo(o1);
                fxFormNode.getProperty().setValue(newValue);
            }
        };
        getElement().addListener(modelChangeListener);
        fxFormNode.getProperty().setValue(getFxForm().getAdapterProvider().getAdapter(getElement().getType(), getNode().getProperty().getClass(), getElement(), getNode()).adaptTo(getElement().getValue()));
    }

    @Override
    protected void unbind(FXFormNode fxFormNode) {
        fxFormNode.getProperty().removeListener(viewChangeListener);
        getElement().removeListener(modelChangeListener);
    }
}