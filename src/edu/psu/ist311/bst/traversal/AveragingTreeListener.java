package edu.psu.ist311.bst.traversal;

import edu.psu.ist311.bst.ISearchTree;

import java.util.Formatter;

public class AveragingTreeListener implements ITreeListener<Integer> {
    private int count = 0;
    private double sum = 0;

    public double getAverage() {
        double avg = sum / count;
        if (count == 0) {
            throw new IllegalStateException(" ");
        }

        return Math.floor(avg * 100) / 100;
    }

    @Override
    public void enterISearchTree(ISearchTree<Integer> e) {
        count++;
        sum = sum + e.data();
    }
}
