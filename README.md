# Binary Search Trees and Hierarchy Listeners/Visitors

### Objectives

There are three objectives to this assignment. First, you will gain experience in implementing generic, nested data structures (in this case, a *binary search tree*). Second, you will continue to practice unit testing and implementation of recursive methods. Last but not least, you will experiment with the visitor design pattern to provide user-customizable, secondary functionality to the tree.

### `ISearchTree` Conceptualization

The interface for our binary search tree is called `ISearchTree` and is generic in `T`, the type of the elements it stores. We can conceptualize this object, binary-tree(T) -- pronounced "binary tree of T" -- as a recursively defined triple containing:
* data : T (i.e.: data 'of type' T)
* left, right : binary-tree(T)

When the search tree is initialized, the left and right subtrees are expected to be the empty-tree (this will correspond to **null** in your implementation). Additionally, the **binary search tree property**: 

> for all *tr* of type binary-tree(T),  
> if *y* is a left descendant of tr, then *y.data* < *tr.data*  
> if *z* is a right descendant of tr, then *z.data* > *tr.data*  

must be maintained throughout your implementation (even between method calls). See the `ISearchTree` interface in the starter kit for the full method contracts. In your implementation, you are required to implement all of the methods specified in the interface.

Note that the implementation you provide does not need to account for duplicate keys. In fact, as part of the contract for `add`, you are to throw a `IllegalArgumentException` if a client tries to add a dup. key to the tree.

## Part 1:  implementing `ISearchTree` (50pts)

Here's a snippet of the implementation to get you started.
```java
public class STTree<E> implements ISearchTree<E> {

  private ISearchTree<E> left, right;
  private E data; 
  // see the course slides for the TWO constructors you should offer
```

The first thing you should do is add the constructors, then get working either `add` **or** the `dumpPreorder` method (consult the canvas slides for the constructors). Then you should override your implementation's `toString` method so you can more easily visualize and draw the shape of the tree when testing/debugging.

Note that the interface requires you to implement two versions of add:
* `add(T e)` recursively adds entry `e` to its correct position
* `addIter(T e)` iteratively adds `e` to its correct position

For `addIter`, you should write a private implementation specific `search` method (discussed in the slides) that uses a while loop to iteratively search for some entry *e* in the tree and returns the subtree containing it, or, failing that, the next closest ancestor/parent node: 

> private ISearchTree < E > search(E e) { ... }

## Part 2: jUnit (20pts)

Once your `addIter` (or `add`) **and** `toString` is working, start writing some unit tests (start writing them early on!)

Some tips for your tests:

* You should write test cases for all of your methods (with the exception of mutators like `setLeft`, `setRight`, and getters like `orderCmp`). Moreover, since `add` and `addIter` both effectively accomplish the same thing, you can reuse your expected inputs and outputs when testing these methods. 
* You should write tests for boundary cases, routine cases, and any tests you consider challenging.
* Practice good unit testing methodology by breaking up your test cases (i.e.: don't clump all of your jUnit asserts in a single `@Test` method, etc.)
* I won't prescribe a fixed number of tests for you to write, use your best judgement on this -- though note that I want to see a nice thorough collection of them (no fewer than 10).

## Part 3: making `ISearchTree` listenable (30pts)

In this last part, you'll experiment with the *[visitor design pattern](https://en.wikipedia.org/wiki/Visitor_pattern)* to make ***what*** is done during visitation of the nodes and subtrees more generic, extensible, and client-customizable. In some sense, the visitor pattern provides users/clients with the ability to enhance the `ISearchTree` interface with an arbitrary number of new algorithms/capabilities -- ***without actually needing to update/add new methods to the `ISearchTree` interface or its subclasses***.

We'll be using a variant of this pattern that constructs a *listener* for the search tree that fires off a callback each time an individual subtree in the search tree hierarchy is 'entered' or 'exited'.

First, define an interface type:
```java
public interface ITreeListener<U> {
  default void enterISearchTree(ISearchTree<U> e) {}
  default void exitISearchTree(ISearchTree<U> e) {}
}
```
* **Note:** the (interface-specific) keyword `default` was introduced in Java 8 and allows users to define so-called `default` methods within interfaces that do not need to be implemented in classes that implement the interface. Default methods can only call other interface methods (so-called primary/core methods)

From Oracle's [documentation](https://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html):
> Default methods enable you to add new functionality to the interfaces of your libraries and ensure binary compatibility with code written for older versions of those interfaces.

Next, you'll need to add a general purpose `accept` method to the `ISearchTree` interface:
```java
void accept(ITreeListener<T> listener); // in ISearchTree.java
```
The method takes any subclass of the `ITreeListener` interface shown above and calls the passed object's `enterISearchTree` and `exitISearchTree` methods mechnically over the internal structure of any `ISearchTree` to achieve an automatic enter-/exit- order traversal. As such, here's the idea for how the `accept` method should be implemented in your `STTree` class:
```
// in STTree.java
@Override void accept(listener):
  // invoke the listener on this ISearchTree...
  listener.enterISearchTree(this)

  // pass along the listener to the left and right ISearchTrees (if they exist, i.e.: not null)
  if left exists then left.accept(listener)
  if right exists then right.accept(listener)

  listener.exitISearchTree(this)
```
notice how the method is invoking the passed `listener`'s implementation of `enterSearchTree` on the current tree, then passing it on (in turn) as arguments to the left and right childrens `accept` method, then calling the invoked `listener`'s exit method once again on the current tree (i.e.: after the children of the current tree have already been visited). **Note: this is tough, you might reread the prior paragraph several times**.

### Example: measuring the max height of a bst using a listener

Suppose we wanted to add the following (missing!) functionality to the `ISearchTree` interface: the ability to determine the maximum height of the 'deepest' subtree in terms of nodes:

<img src="https://github.com/dtwelch/resources/blob/master/311fa21/asg4/levels3.png" width="520">

Normally we'd have to add a method `int depth()` to the `ISearchTree` interface that recursively invokes the method on the `left` and `right` children. Instead, now we can just extend the `ITreeListener` interface as follows:

```java
public final class MaxDepthComputingListener<R> implements ITreeListener<R> {
    public int maxDepthSoFar, scratchDepth;

    public MaxDepthComputingListener() { this.scratchDepth = 0; }

    public int getMaxDepth() { return maxDepthSoFar; }

    @Override public void enterISearchTree(ISearchTree<R> e) {
        if (scratchDepth > maxDepthSoFar) { // are we in a subtree that's deeper than any seen before?
            maxDepthSoFar = scratchDepth;   // if so, reassign max height we've seen
        }
        scratchDepth++;
    }

    @Override public void exitISearchTree(ISearchTree<R> e) {
        scratchDepth--;
    }
}
```

Here's some code from a test that builds the tree from the right hand side tree in the above picture then computes the max height of it using the listener class defined above:
```java
ISearchTree<Integer> bst =           // builds the 'host' tree from the fig above (right hand side)
      new STTree<>(7, Integer::compareTo)
              .add(4).add(11)
              .add(3).add(6)
              .add(5).add(12);

MaxDepthComputingListener<Integer> heightListener = new MaxHeightComputingListener<>();
bst.accept(heightListener); // passing the listener to the host tree
Assertions.assertEquals(3, heightListener.getMaxHeight());
```
The code above creates an instance of the `MaxHeightComputingListener` class and passes it into the `bst` host tree's `accept` method (i.e.: `bst.accept(heightListener)`). 

This line triggers a pre- and post-order traversal through the host tree with custom functionality defined to occur each time a node/subtree is entered and exited (in this case, the 'custom' functionality is defined in the `MaxHeightComputingListener` class). The final result (the max height) can be retrieved by calling `getMaxHeight` after the traversal has completed.

### Problems:

*Any classes created for the problems below should be placed in the `edu.psu.ist311.traversal` package.*

1. Write a listener class, `AveragingTreeListener` that implements `ITreeListener<Integer>` (i.e.: it's a listener class for `ISearchTree`s consisting of integers). Your listener class should provide a single method `getAverage` that returns the `int` average of all entries stored in the tree. The average should be computed during the listener's traversal through the tree. It's up to you whether you compute the sum and/or node count in an @Override of the `enterISearchTree` or `exitISearchTree` method (should be one or the other -- not both). Think about whether it even matters for this particular problem. Add an @Test for this listener to your testing class.

2. Write a generically typed listener class, `ContainmentCheckingListener<E>` that implements `ITreeListener<E>`. The listener extension class should override either `enterISearchTree` or `exitISearchTree` and set a global boolean variable (within `ContainmentCheckingListener`) to true if and only if you encounter an `ISearchTree` node whose data `equals` some `key` of type `E` supplied to the constructor for the listener. For testing, make your `contains` method in `STTree` instantiate and pass the listener to `this` STTree's `accept` method. The unit tests you wrote for `contains` in part 2 should still pass after you make these changes.

<!-- ### Optional challenge question (involving higher-order predicates):

Define a listener that counts the number of 'shapes' that occur in a given `ISearchTree`. 

Before getting to the question, a little background and terminology for what you'll use to complete this problem (the problem is specified below)

#### Some background on higher order functions and predicates

A `Predicate<T>` is a type introduced in Java 8 that represents all boolean-valued functions (i.e.: those that return only true or false) that operate on a single generic argument argument of type `T`. 

For example, a *predicate* for testing whether a given integer *x* is even could be written as follows:
```java
Predicate<Integer> isEven = (Integer x) -> x % 2 == 0; 
```
We could write an equivalent version of the above using the higher order `Function` class (also introduced in Java 8):
```java
// same as above -- but this one is written (explicitly) as a function that 
// takes an Integer and produces/returns a Boolean
Function<Integer, Boolean> isEven = (Integer x) -> x % 2 == 0; 
```
In both examples above, the `(Integer x)` introduces a formal parameter named `x` of type `Integer`. The part after the arrow `->` denotes the *body* of the function or predicate. In this case, all the body is doing is asserting that `x % 2 == 0` (which is of type boolean). Technically, you are not even required to specify the type in front of the formal parameter, so even this would be acceptable:
```java
Function<Integer, Boolean> isEven = x -> x % 2 == 0; // java's type system infers the type of x from the body
```

The body can also consist of multiple lines of code if written using curly braces like so:
```
Function<Integer, Integer> addTwice = (Integer x) -> {
          int result = x + x;
          return result;
       }; 
```
-->

## Handin and Grading

When you are ready to submit (or simply want to 'checkin' your work), commit your work by typing:

> git commit -am "message goes here"

then follow this up with a

> git push origin main

You will be graded on the quality of your implementation, the quality and breadth of your jUnit tests, and your implementation of the specified listeners.
