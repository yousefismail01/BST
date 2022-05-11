package edu.psu.ist311.bst.traversal;

import edu.psu.ist311.bst.ISearchTree;

public interface ITreeListener<U> {

    /**
     * An <em>enter</em> method for an {@link ISearchTree} node. Default
     * implementation does nothing.
     */
    default void enterISearchTree(ISearchTree<U> e) {
    }

    /**
     * An <em>exit</em> method for an {@link ISearchTree} node. Default
     * implementation does nothing.
     */
    default void exitISearchTree(ISearchTree<U> e) {
    }

}
