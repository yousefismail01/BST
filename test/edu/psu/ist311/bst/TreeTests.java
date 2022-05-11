package edu.psu.ist311.bst;

import edu.psu.ist311.bst.traversal.AveragingTreeListener;
import edu.psu.ist311.bst.traversal.MaxDepthComputingListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TreeTests {

    @Test
    public void testHeightComputingListener() {

        ISearchTree<Integer> bst =
                new STTree<>(10, Integer::compareTo)
                        .add(12).add(5)
                        .add(4).add(20)
                        .add(8).add(7).add(15);

        MaxDepthComputingListener<Integer> l = new MaxDepthComputingListener<>();
        bst.accept(l);
        Assertions.assertEquals(3, l.getMaxDepth());

        // now add 16...
        bst = bst.add(16);
        l = new MaxDepthComputingListener<>();
        bst.accept(l);
        Assertions.assertEquals(4, l.getMaxDepth());
    }

    @Test
    public void testBstAdd() {
        ISearchTree<Integer> bst =
                new STTree<>(10, Integer::compareTo)
                        .add(12).add(5).add(3);
        bst.add(4);

        Assertions.assertEquals("10, 5, 3, 4, 12", bst.toString());
    }

    @Test
    public void testBstAddFailure() {
        ISearchTree<Integer> bst =
                new STTree<>(10, Integer::compareTo).
                        add(12).add(5).add(3);


        Assertions.assertThrows(IllegalArgumentException.class, () -> bst.add(3));
    }

    @Test
    public void testBstAddIter() {
        ISearchTree<Integer> bst =
                new STTree<>(10, Integer::compareTo)
                        .add(12).add(5).add(3);
        bst.addIter(9);
        bst.add(6);

        Assertions.assertEquals("10, 5, 3, 9, 6, 12", bst.toString());
    }

    @Test
    public void testBstAddIterFailure() {
        ISearchTree<Integer> bst =
                new STTree<>(10, Integer::compareTo).
                        add(12).add(5).add(3);


        Assertions.assertThrows(Exception.class, () -> bst.addIter(10));
    }

    @Test
    public void testDumpPreorder() {
        ISearchTree<Integer> bst =
                new STTree<>(10, Integer::compareTo).
                        add(12).add(5).add(3);
        bst.dumpPreorder();

        Assertions.assertEquals("10, 5, 3, 12", bst.toString());
    }

    @Test
    public void testDumpPostorder() {
        ISearchTree<Integer> bst =
                new STTree<>(10, Integer::compareTo).
                        add(12).add(5).add(3);
        bst.dumpPostorder();

        Assertions.assertEquals("3, 5, 12, 10", bst.dumpPostorder());
    }

    @Test
    public void testContains1() {
        ISearchTree<Integer> bst =
                new STTree<>(10, Integer::compareTo);


        Assertions.assertTrue(bst.contains(10));
    }

    @Test
    public void testContains2() {
        ISearchTree<Integer> bst =
                new STTree<>(10, Integer::compareTo).
                        add(12);

        Assertions.assertTrue(bst.contains(12));
    }

    @Test
    public void testContains3() {
        ISearchTree<Integer> bst =
                new STTree<>(10, Integer::compareTo).
                        add(12).add(5).add(3);

        Assertions.assertTrue(bst.contains(5));
    }

    @Test
    public void testAverage() {
        {
            ISearchTree<Integer> bst =
                    new STTree<>(10, Integer::compareTo)
                            .add(12).add(5)
                            .add(4).add(20)
                            .add(8).add(7).add(15);

            AveragingTreeListener l = new AveragingTreeListener();

            // now add 16...
            bst = bst.add(16);
            l = new AveragingTreeListener();
            bst.accept(l);
            Assertions.assertEquals(10.77, l.getAverage());
        }

    }
}

