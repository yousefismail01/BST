package edu.psu.ist311.bst;

import edu.psu.ist311.bst.traversal.ContainsListener;
import edu.psu.ist311.bst.traversal.ITreeListener;

import java.util.Comparator;

// todo: implementation of ISearchTree in here
public class STTree<E> implements ISearchTree<E> {

    private ISearchTree<E> left;
    private ISearchTree<E> right;
    private E data;
    private final Comparator<E> order;

    private STTree(E data, ISearchTree<E> l, ISearchTree<E> r, Comparator<E> o) {
        this.left = l;
        this.right = r;
        this.data = data;
        this.order = o;
    }

    //leaf contructor...
    public STTree(E data, Comparator<E> o) {
        this(data, null, null, o);
    }

    @Override
    public E data() {
        return data;
    }

    @Override
    public Comparator<E> orderCmp() {
        return order;
    }

    @Override
    public ISearchTree<E> left() {
        return left;
    }

    @Override
    public ISearchTree<E> right() {
        return right;
    }


    //(1) add(E e)
    //(2) dumpPreorder()
    //(3) implement the search method

    //recursive version of add
    @Override
    public ISearchTree<E> add(E e) {
        if (order.compare(e, this.data) < 0) {
            if (left != null) {
                left.add(e);
            } else {
                this.setLeft(new STTree<E>(e, order));
            }
        } else if (order.compare(e, this.data) > 0) {
            if (right != null) {
                right.add(e);
            } else {
                this.setRight(new STTree<E>(e, order));
            }
        } else throw new IllegalArgumentException("dup key");

        return this;
    }


    private ISearchTree<E> search(E key) {
        //return the closest node where we want to attach a new tree
        ISearchTree<E> v = this;
        boolean absent = false;

        while (!absent && key != v.data()) {
            if (order.compare(key, v.data()) < 0 && v.left() != null) {
                v = v.left();
            } else if (order.compare(key, v.data()) > 0 && v.right() != null) {
                v = v.right();
            } else {
                absent = true;
            }

        }
        return v;
    }


    //iterative version of add...
    @Override
    public ISearchTree<E> addIter(E e) {
        //call search(e)
        ISearchTree<E> placeToAttach = search(e);

        if (order.compare(e, placeToAttach.data()) < 0) {
            placeToAttach.setLeft(new STTree<E>(e, order));
        } else if (order.compare(e, placeToAttach.data()) > 0) {
            placeToAttach.setRight(new STTree<E>(e, order));

        } else throw new IllegalArgumentException("dup key");

        return this;
    }

    //figure out which side to attach the returned "node/tree" to


    @Override
    public boolean contains(E e) {
        //return search(e).data().equals(e);
        ContainsListener<E> l = new ContainsListener<E>(e);
        this.accept(l);

        return l.isFound();
    }

    @Override
    public void setLeft(ISearchTree<E> l) {
        this.left = l;
    }

    @Override
    public void setRight(ISearchTree<E> r) {
        this.right = r;
    }

    @Override
    public String dumpPreorder() {
        String result = this.data.toString();

        if (left != null) {
            result += ", " + left.dumpPreorder();
        }
        if (right != null) {
            result += ", " + right.dumpPreorder();
        }
        return result;
    }


    @Override
    public String dumpPostorder() {
       /* StringBuilder traversal = new StringBuilder();

        this.accept(new ITreeListener<E>() {
            @Override
            public void exitISearchTree(ISearchTree<E> e) {
                traversal.append(e.data()).append(", ");
            }

        });
        return traversal.toString();*/
        String result = "";

        if (left != null) {
            result += left.dumpPostorder() + ", ";
        }
        if (right != null) {
            result += right.dumpPostorder() + ", ";
        }
        result += this.data;

        return result;
    }

    @Override
    public String toString() {
        return dumpPreorder();
    }

    @Override
    public void accept(ITreeListener<E> listener) {
        listener.enterISearchTree(this);

        if (left != null) {
            left.accept(listener);
        }
        if (right != null) {
            right.accept(listener);
        }
        listener.exitISearchTree(this);
    }
}
