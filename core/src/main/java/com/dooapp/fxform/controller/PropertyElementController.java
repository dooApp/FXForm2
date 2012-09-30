package com.dooapp.fxform.controller;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.model.PropertyElement;
import com.dooapp.fxform.view.FXFormSkin;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;

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

    protected ObservableList<ConstraintViolation> constraintViolations;

    private final NodeController constraintController;

    public PropertyElementController(FXForm fxForm, PropertyElement element) {
        super(fxForm, element);
        constraintController = new ConstraintController(fxForm, element, constraintViolations);
        updateSkin((FXFormSkin) fxForm.getSkin());
        constraintViolations.addListener(new ListChangeListener<ConstraintViolation>() {

            @Override
            public void onChanged(Change<? extends ConstraintViolation> change) {
                if (constraintViolations.size() > 0) {
                    if (labelController.getNode() != null)
                        labelController.getNode().getNode().getStyleClass().add(FXForm.LABEL_STYLE + FXForm.INVALID_STYLE);
                    if (editorController.getNode() != null)
                        editorController.getNode().getNode().getStyleClass().add(FXForm.EDITOR_STYLE + FXForm.INVALID_STYLE);
                    if (tooltipController.getNode() != null)
                        tooltipController.getNode().getNode().getStyleClass().add(FXForm.TOOLTIP_STYLE + FXForm.INVALID_STYLE);
                } else {
                    if (labelController.getNode() != null)
                        labelController.getNode().getNode().getStyleClass().remove(FXForm.LABEL_STYLE + FXForm.INVALID_STYLE);
                    if (editorController.getNode() != null)
                        editorController.getNode().getNode().getStyleClass().remove(FXForm.EDITOR_STYLE + FXForm.INVALID_STYLE);
                    if (tooltipController.getNode() != null)
                        tooltipController.getNode().getNode().getStyleClass().remove(FXForm.TOOLTIP_STYLE + FXForm.INVALID_STYLE);
                }
            }
        });
    }

    public ObservableList<ConstraintViolation> getConstraintViolations() {
        return constraintViolations;
    }

    @Override
    protected NodeController createEditorController(FXForm fxForm, Element element) {
        constraintViolations = FXCollections.observableArrayList(new ArrayList<ConstraintViolation>());
        return new PropertyEditorController(fxForm, element, constraintViolations);
    }

    @Override
    protected void updateSkin(FXFormSkin skin) {
        super.updateSkin(skin);
        if (constraintController != null)
            constraintController.setNode(skin.getConstraint(element));
    }
}