package com.dooapp.fxform.controller;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.PropertyElement;
import com.dooapp.fxform.view.FXFormSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;

/**
 * Created at 27/09/12 13:51.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class PropertyElementController<WrappedType> extends ElementController<WrappedType> {
    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(PropertyElementController.class);

    protected ObservableList<ConstraintViolation> constraintViolations = FXCollections.observableArrayList();

    private final NodeController constraintController;

    public PropertyElementController(FXForm fxForm, PropertyElement element) {
        super(fxForm, element);
        constraintController = new ConstraintController(fxForm, element, constraintViolations);
    }

    public ObservableList<ConstraintViolation> getConstraintViolations() {
        return constraintViolations;
    }

    @Override
    protected NodeController createEditorController(FXForm fxForm, Element element) {
        return new PropertyEditorController(fxForm, element, constraintViolations);
    }

    @Override
    protected void updateSkin(FXFormSkin skin) {
        constraintController.setNode(skin.getConstraint(element));
    }
}