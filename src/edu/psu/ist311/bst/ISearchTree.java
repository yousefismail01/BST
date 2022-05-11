package edu.psu.ist311.bst;

import edu.psu.ist311.bst.traversal.ITreeListener;

import java.util.Comparator;

/**
 * A binary search tree (BST) interface conceptualization.
 * <p>
 * Conceptually, this object, binary-tree(T) -- pronounced: "binary tree of T",
 * is recursively defined as a triple containing:
 *  <ol>
 *      <li>data : T (i.e.: data 'of type' T)</li>
 *      <li>left, right : binary-tree(T) </li>
 * </ol>
 * <p>
 * <b>initially:</b> this.left = empty_tree and this.right = empty_tree and let
 * data-set(tr) denote the set of all data entries stored in the tree tr and
 * its subtrees.
 * <p>
 * <b>constraints:</b> this tree is conformal with binary search tree
 * ordering property. Assume we have a predicate, is-ordered(e, tr), which
 * produces {@code true} if and only if the entry e : T is properly ordered
 * w.r.t. the binary-tree(T), tr, passed; {@code false} otherwise.
 *
 * @param <T> The type of the entries stored inside the tree
 */
public interface ISearchTree<T> {

    T data();

    /**
     * Returns the {@link Comparator} used to impose a total ordering on the
     * type of this tree's internal data (i.e.: type {@code T})
     * <p>
     * <b>Note:</b> it is up to the client to ensure their comparator is a total
     * ordering over domain {@code T}.
     */
    Comparator<T> orderCmp();

    ISearchTree<T> left();

    ISearchTree<T> right();

    /**
     * Returns the root of {@code this} search tree with {@code e} added to the
     * appropriate subtree.
     * <p>
     * <strong>Note:</strong> when implementing this recursive version of add,
     * do not use the <em>iterative</em> "search" method from the slides.
     * <p>
     * <b>requires</b> e not-in data-set(this)<p>
     * <b>ensures</b> if old(this) = empty_tree
     *     then this = (e, empty-tree, empty-tree) <p>
     *     else [entry e is added to the tree] and is-ordered(e, this) --
     *   i.e.: the output tree is ordered w.r.t. entry e
     *
     * @throws IllegalArgumentException if a duplicate key is added.
     */
    ISearchTree<T> add(T e);

    /**
     * See {@link #add(T)} for the contract. Same exceptions apply.
     */
    ISearchTree<T> addIter(T e);

    /**
     * Returns {@code true} if key {@code e} is contained in this search tree;
     * {@code false} otherwise.
     * <p>
     * <b>ensures</b> true iff e is-in data-set(this); false otherwise.
     */
    boolean contains(T e);

    void setLeft(ISearchTree<T> l);

    void setRight(ISearchTree<T> r);

    // builtin traversals

    /**
     * <b>ensures:</b> a string containing the preorder/enterorder traversal of
     * this search tree is returned.
     */
    String dumpPreorder();



    /**
     * <b>ensures:</b> a string containing the postorder/exitorder traversal of
     * this search tree is returned.
     */
    String dumpPostorder();

    /**
     * Returns a string containing a pre-order traversal of this tree.
     * <p>
     * <b>Note:</b> this should just call and return the result of
     * {@link #dumpPreorder()}.
     * <p>
     * <b>ensures:</b> the string containing the preorder traversal
     *         of this tree is returned.
     */
    String toString();

    void accept(ITreeListener<T> listener); // in ISearchTree.java
}
