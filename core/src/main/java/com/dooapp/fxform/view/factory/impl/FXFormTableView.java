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

package com.dooapp.fxform.view.factory.impl;

import com.dooapp.fxform.FXForm;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;

/**
 * A TableView which opens a popup with an FXForm to edit the selected items.
 * <p/>
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 26/11/2013
 * Time: 14:13
 */
public class FXFormTableView extends TableView {

    Popup popup;

    final FXForm fxForm = new FXForm();

    double dragX;
    double dragY;
    double popupX;
    double popupY;

    public FXFormTableView() {
        fxForm.sourceProperty().bind(getSelectionModel().selectedItemProperty());
        addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (getSelectionModel().getSelectedItem() == null)
                    return;
                if (popup == null) {
                    createPopup();
                }
                if (!popup.isShowing()) {
                    popup.show(FXFormTableView.this, mouseEvent.getScreenX() + 15, mouseEvent.getScreenY() - popup.getHeight() / 2);
                }
            }
        });
    }

    protected void createPopup() {
        popup = new Popup();
        popup.getContent().add(fxForm);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        popup.setAutoFix(true);
        popup.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dragX = mouseEvent.getScreenX();
                dragY = mouseEvent.getScreenY();
                popupX = popup.getX();
                popupY = popup.getY();
            }
        });
        popup.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                popup.setX(popupX + mouseEvent.getScreenX() - dragX);
                popup.setY(popupY + mouseEvent.getScreenY() - dragY);
            }
        });
    }
}
