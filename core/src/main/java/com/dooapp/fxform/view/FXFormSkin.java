/*
 * Copyright (c) 2011, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform.view;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.controller.ElementController;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 09/04/11
 * Time: 21:36
 * Skin of the FXForm control.
 */
public abstract class FXFormSkin implements Skin<FXForm> {

    private final Logger logger = LoggerFactory.getLogger(FXFormSkin.class);

    protected FXForm fxForm;

    private Node rootNode;

    public FXFormSkin(FXForm fxForm) {
        this.fxForm = fxForm;
    }

    protected abstract Node createRootNode() throws NodeCreationException;

    protected ListChangeListener controllersListener;

    public Node getNode() {
        if (rootNode == null) {
            logger.debug("Creating skin node");
            try {
                rootNode = createRootNode();
                addControllers(fxForm.getControllers());
                fxForm.getControllers().addListener(getControllersListener());
            } catch (NodeCreationException e) {
                e.printStackTrace();
            }
        }
        return rootNode;
    }

    public ListChangeListener getControllersListener() {
        if (controllersListener == null) {
            controllersListener = new ListChangeListener() {
                public void onChanged(Change change) {
                    logger.debug("Updating controllers view");
                    while (change.next()) {
                        addControllers(change.getAddedSubList());
                        if (change.wasRemoved()) {
                            removeControllers(change.getRemoved());
                            unregisterControllers(change.getRemoved());
                        }
                    }
                }
            };
        }
        return controllersListener;
    }

    private void unregisterControllers(List<ElementController> removed) {
        logger.debug("Clearing controllers nodes");
        for (ElementController controller : removed) {
            controller.dispose();
        }
    }

    protected abstract void removeControllers(List<ElementController> removed);

    protected abstract void addControllers(List<ElementController> addedSubList);

    public Node getLabel(ElementController controller) {
        try {
            return controller.getLabel().getNode();
        } catch (NodeCreationException e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    public Node getTooltip(ElementController controller) {
        try {
            return controller.getTooltip().getNode();
        } catch (NodeCreationException e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    public Node getEditor(ElementController controller) {
        try {
            return controller.getEditor().getNode();
        } catch (NodeCreationException e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    public Node getConstraint(ElementController controller) {
        try {
            return controller.getConstraint().getNode();
        } catch (NodeCreationException e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    public void dispose() {
        logger.debug("Disposing skin");
        for (Object controller : fxForm.getControllers()) {
            ElementController controller1 = (ElementController) controller;
            try {
                controller1.getConstraint().dispose();
                controller1.getEditor().dispose();
                controller1.getLabel().dispose();
                controller1.getTooltip().dispose();
            } catch (NodeCreationException e) {
                logger.warn(e.getMessage(), e);
            }
        }
        fxForm.getControllers().removeListener(getControllersListener());
        fxForm = null;
    }

    public FXForm getSkinnable() {
        return fxForm;
    }

}
