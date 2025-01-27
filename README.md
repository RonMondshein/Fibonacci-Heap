# Fibonacci Heap Implementation and Analysis

This repository contains an implementation of a **Fibonacci Heap**, a powerful data structure for priority queue operations, according to an assignment. Fibonacci Heap is a data structure used for implementing priority queues with efficient insertions, deletions, and extract-min operations. It is a collection of trees with a "lazy" structure that allows for marked nodes to be consolidated later. The key properties of Fibonacci Heap are: the minimum element is stored at the root, each node has an associated degree, each tree satisfies the minimum-heap property, and the degree of any node is at most O(log n). Fibonacci Heap has an amortized time complexity of O(1) for insert, find-min, decrease-key and meld operations and O(log n) for delete-min, where n is the number of nodes in the heap.

## File Structure

- `FibonacciHeap_fullcode.java`: Complete implementation of the Fibonacci Heap, including all necessary operations and helper methods.
- `Theoretical_Part.docx` & `Theoretical_Part.pdf`: Documentation explaining the implementation, complexity analysis, and experimental results.
- `Assignment.pdf`: Task description and project requirements.
