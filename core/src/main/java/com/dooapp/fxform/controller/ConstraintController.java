package com.dooapp.fxform.controller;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.FXFormNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;

/**
 * Created at 28/09/12 10:22.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class ConstraintController extends NodeController {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ConstraintController.class);

    private final ObservableList<ConstraintViolation> constraintViolations;

    public ConstraintController(FXForm fxForm, Element element, ObservableList<ConstraintViolation> constraintViolations) {
        super(fxForm, element);
        this.constraintViolations = constraintViolations;
    }

    @Override
    protected void bind(FXFormNode fxFormNode) {
        fxFormNode.getProperty().setValue(constraintViolations);
    }

    @Override
    protected void unbind(FXFormNode fxFormNode) {
        fxFormNode.getProperty().setValue(FXCollections.emptyObservableList());
    }
}