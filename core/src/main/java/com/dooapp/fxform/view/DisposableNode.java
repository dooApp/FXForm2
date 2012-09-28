package com.dooapp.fxform.view;

import javafx.scene.Node;

/**
 * Created at 27/09/12 11:38.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public interface DisposableNode<N extends Node> extends Disposable {

    /**
     * Get the encapsulated node.
     *
     * @return
     */
    public N getNode();

}
