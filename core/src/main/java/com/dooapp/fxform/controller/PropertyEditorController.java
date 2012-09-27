package com.dooapp.fxform.controller;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.PropertyElement;
import com.dooapp.fxform.view.factory.FXFormNode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.*;
import java.util.Set;

/**
 * Created at 27/09/12 17:32.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class PropertyEditorController extends NodeController {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(PropertyEditorController.class);

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
            logger.trace("Validation disabled", e);
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
                Object newValue = getFxForm().getAdapter(getNode(), getElement()).adaptFrom(o1);
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
                Object newValue = getFxForm().getAdapter(getNode(), getElement()).adaptTo(o1);
                fxFormNode.getProperty().setValue(o1);
            }
        };
        getElement().addListener(modelChangeListener);
    }

    @Override
    protected void unbind(FXFormNode fxFormNode) {
        fxFormNode.getProperty().removeListener(viewChangeListener);
        getElement().removeListener(modelChangeListener);
    }
}