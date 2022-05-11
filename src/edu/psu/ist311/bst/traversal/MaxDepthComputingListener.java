package edu.psu.ist311.bst.traversal;

import edu.psu.ist311.bst.ISearchTree;

public final class MaxDepthComputingListener<R> implements ITreeListener<R> {
    public int maxDepthSoFar, scratchDepth;

    public MaxDepthComputingListener() {
        this.scratchDepth = 0;
    }

    public int getMaxDepth() {
        return maxDepthSoFar;
    }

    @Override
    public void enterISearchTree(ISearchTree<R> e) {
        if (scratchDepth > maxDepthSoFar) {
            maxDepthSoFar = scratchDepth;
        }
        scratchDepth++;
    }

    @Override
    public void exitISearchTree(ISearchTree<R> e) {
        scratchDepth--;
    }


}
