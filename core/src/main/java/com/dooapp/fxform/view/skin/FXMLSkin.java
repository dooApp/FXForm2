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

package com.dooapp.fxform.view.skin;

import com.dooapp.fxform.FXForm;
import com.dooapp.fxform.model.Element;
import com.dooapp.fxform.view.NodeCreationException;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * Created at 26/09/12 13:49.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class FXMLSkin extends NodeSkin {

    private final Logger logger = Logger.getLogger(FXMLSkin.class.getName());

    private final URL url;

    private FXMLLoader fxmlLoader;

    private Initializable controller;

    public FXMLSkin(FXForm fxForm, URL url) {
        this(fxForm, url, null);
    }

    public FXMLSkin(FXForm fxForm, URL url, Initializable controller) {
        super(fxForm);
        this.url = url;
        this.controller = controller;
        setOnCreateNode(new Callable<Node>() {
            @Override
            public Node call() throws Exception {
                return loadFxml();
            }
        });
        buildNode();
    }

    protected Node loadFxml() throws NodeCreationException {
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(url);
		fxmlLoader.setResources(getSkinnable().getResourceBundle());
        fxmlLoader.setController(controller);
        try {
            return (Node) fxmlLoader.load();
        } catch (IOException e) {
            throw new NodeCreationException(e.getMessage(), e);
        }
    }

    @Override
    protected Node lookupNode(Element element, String suffix) {
        Node result = super.lookupNode(element, suffix);
        if (result != null) {
            return result;
        } else {
            // lookup by id failed, try to get node from FXMLLoader namespace
            Optional<String> key = fxmlLoader.getNamespace().keySet().stream().filter(k ->
                    k.toLowerCase().equals((element.getName() + suffix)
                            .replaceAll("-", "").toLowerCase()))
                    .findFirst();
            if (key.isPresent()) {
                return (Node) fxmlLoader.getNamespace().get(key.get());
            }
        }
        return null;
    }
}