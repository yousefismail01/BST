package edu.psu.ist311.bst.traversal;

import edu.psu.ist311.bst.ISearchTree;

public class ContainsListener<E> implements ITreeListener<E> {
    private final E key;
    private boolean found = false;

    public ContainsListener(E key) {
        this.key = key;
    }

    @Override
    public void exitISearchTree(ISearchTree<E> e) {
        if (key.equals(e.data())) {
            found = true;
        }
    }

    public boolean isFound() {
        return found;
    }
}
