/** FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
/*
username1 - ronmondshein
id1      - 318895877
name1    - Ron Mondshein
username2 - pollinger
id2      - 318688991
name2    - Eden Pollinger
*/
public class FibonacciHeap {
	private HeapNode min;
	private HeapNode first;
	private int totalMarked;
	private static int totalCuts;
	private static int totalLinks;
	private int totalTrees;

	private int size; // Number of total nodes in the tree

	public FibonacciHeap() {
		// Initialize new FibonacciHeap
		min = null;
		first = null;
		size = 0;
		totalCuts = 0;
		totalMarked = 0;
		totalLinks = 0;
	}
	
	/**
	 * public int getMin()
	 *
	 * Returns the value of a node if and only if the heap is not empty. Otherwise
	 * return 0.
	 * 
	 */

	public int getMin() {
		if (this.findMin() == null) {
			return 0;
		}
		return this.findMin().key;
	}

	/**
	 * public boolean isEmpty()
	 *
	 * Returns true if and only if the heap is empty.
	 * 
	 */
	public boolean isEmpty() {
		return min == null;
	}

	/**
	 * public HeapNode insert(int key)
	 *
	 * Creates a node (of type HeapNode) which contains the given key, and inserts
	 * it into the heap. The added key is assumed not to already belong to the heap.
	 * 
	 * Returns the newly created node.
	 * 	 * complexity of O(1)
	 */
	public HeapNode insert(int key) {
		HeapNode node = new HeapNode(key);
		insertNode(node);
		size++;
		return node;
	}

	public HeapNode insertNode(HeapNode node) {
		if (min != null) {
			node.next = first.next;
			first.next.prev = node;
			first.next = node;
			node.prev = first;
			first = node;
			if (node.key < min.key) {
				min = node;
			}
		} else {
			// The tree is empty so there isn't any minNode or first
			min = node;
			first = node;
			min.next = node;
			min.prev = node;
		}
		totalTrees++;
		return node;
	}

	/**
	 * public void deleteMin()
	 *
	 * Deletes the node containing the minimum key.
	 * complexity of O(logn)
	 */

	public void deleteMin() {
		if(!isEmpty())
			{
			if (size == 1) {
				min = null;
				first = null;
				size = 0;
				totalTrees = 0;
				return;
			}
	
			if (min.child != null) {
				HeapNode child = min.child;
				HeapNode curr = child;
				do {
					curr.parent = null;
					if(curr.mark)
					{
						curr.mark = false;
						totalMarked--;
					}
					curr = curr.next;
				} while (curr != child);
				
				if (first == min)
					first = min.child;
				
				if (min.next != min) {
					HeapNode orig_min_next = min.next;
					HeapNode orig_min_child = min.child;
					HeapNode orig_min_child_next = min.child.next;
					HeapNode orig_min_prev = min.prev;
	
					orig_min_next.prev = orig_min_child;
					orig_min_child.next = orig_min_next;
	
					orig_min_prev.next = orig_min_child_next;
					orig_min_child_next.prev = orig_min_prev;
				}
				totalTrees += min.rank - 1;
	
	
			} 
			else
			{
				if (first == min)
					first = min.prev;
				min.prev.next = min.next;
				min.next.prev = min.prev;
				totalTrees --;
	
			}
			consolidate();
			size--;
		}
	}

	/**
	 * private void consolidate()
	 *
	 * Reorganize the heap after a node is removed from the root list. Used in
	 * deleteMin
	 * complexity of O(logn)
	 *
	 */

	private void consolidate() {
		int maxDegree = ((int) (Math.log(size) / Math.log(2))); // Determine the size of the array of "buckets" that is
//		 used to reorganize the heap.
		HeapNode[] roots = new HeapNode[maxDegree + 1]; // "buckets" array of HeapNode

		HeapNode[] og_roots = new HeapNode[totalTrees];
		HeapNode currRoot = first;
		for (int i = 0; i < og_roots.length; ++i) {
			og_roots[i] = currRoot;
			currRoot = currRoot.next;
		}

		for (int i = 0; i < og_roots.length; ++i) {
			currRoot = og_roots[i];
			while (roots[currRoot.rank] != null) {
				if (currRoot.getKey() > roots[currRoot.rank].getKey()) {
					HeapNode temp = currRoot;
					currRoot = roots[currRoot.rank];
					roots[currRoot.rank] = temp;
				}

				link(currRoot, roots[currRoot.rank]);
				roots[currRoot.rank] = null;
				if(currRoot.rank < maxDegree)
					currRoot.rank++;
				else
					return;
			}
			roots[currRoot.rank] = currRoot;
		}

		first = null;
		min = null;
		totalTrees = 0;
		for (int i = roots.length - 1; i >= 0; i--) {
			if (roots[i] != null)
				insertNode(roots[i]);
		}
	}
	
	/**
	 * private void link((HeapNode x, HeapNode y))
	 *
	 * Linking two heapNodes together so one of them becomes the child of the other. 
	 * complexity of O(1)
	 */

	private void link(HeapNode x, HeapNode y) {
		totalTrees--;
		if (first == y)
			first = y.next;

		if (x.child == null) {
			x.child = y;
			y.parent = x;
			y.prev.next = y.next;
			y.next.prev = y.prev;
			y.next = y;
			y.prev = y;
		} else {
			HeapNode y_next = y.next;
			HeapNode y_prev = y.prev;

			HeapNode x_child = x.child;
			HeapNode x_child_next = x.child.next;

			y_next.prev = y_prev;
			y_prev.next = y_next;

			y.next = x_child_next;
			x_child_next.prev = y;

			y.prev = x_child;
			x_child.next = y;

			y.parent = x;
			x.child = y;

		}
		totalLinks++;
	}

	/**
	 * public HeapNode findMin()
	 *
	 * Returns the node of the heap whose key is minimal, or null if the heap is
	 * empty.
	 * complexity of O(1)
	 */
	public HeapNode findMin() {
		if (isEmpty()) {
			// If the heap is empty
			return null;
		}
		return min;
	}

	/**
	 * public void meld (FibonacciHeap heap2)
	 *
	 * Melds heap2 with the current heap.
	 * complexity of O(1)
	 */
	public void meld(FibonacciHeap heap2) {
		if(!heap2.isEmpty())
		{
			if(isEmpty())
			{
				first = heap2.first;
				min = heap2.min;
				size = heap2.size;
			}
			
			else
			{		
				HeapNode firstHeapFirst = first;
				HeapNode firstHeapLast = first.next;
				HeapNode secondHeapFirst = heap2.first;
				HeapNode secondHeapLast = heap2.first.next;
	
				secondHeapFirst.next = firstHeapLast;
				secondHeapLast.prev = firstHeapFirst;
				firstHeapFirst.next = secondHeapLast;
				firstHeapLast.prev = secondHeapFirst;
				
				if (heap2.min.getKey() < min.getKey())
					min = heap2.min;
			}
			
			totalTrees += heap2.totalTrees;
			totalMarked += heap2.totalMarked;
			size += heap2.size;
		}
	}

	/**
	 * public int size()
	 *
	 * Returns the number of elements in the heap.
	 * complexity of O(1) 
	 */
	public int size() {
		return size;
	}

	/**
	 * public int[] countersRep()
	 *
	 * Return an array of counters. The i-th entry contains the number of trees of
	 * order i in the heap. (Note: The size of of the array depends on the maximum
	 * order of a tree.) complexity of O(n)
	 * 
	 */
	public int[] countersRep() {
		int maxDegree = (int) (Math.log(size) / Math.log(2)) + 1;
		int[] ranks = new int[maxDegree];
		if (isEmpty()) {
			return new int[0];
		}
		HeapNode current = min;
		do {
			ranks[current.rank]++;
			current = current.next;
		} while (current != min);
		int lastNonZero = ranks.length - 1;
		while (lastNonZero >= 0 && ranks[lastNonZero] == 0) {
			lastNonZero--;
		}
		int newLength = lastNonZero + 1;
		int[] newRanks = new int[newLength];

		for (int j = 0; j < newRanks.length; j++) {
			newRanks[j] = ranks[j];
		}
		return newRanks;

	}

	/**
	 * public void delete(HeapNode x)
	 *
	 * Deletes the node x from the heap. It is assumed that x indeed belongs to the
	 * heap.
	 * complexity of O(n)
	 *
	 */
	public void delete(HeapNode x) {
		decreaseKey(x, Integer.MAX_VALUE);// making the node the new min
		deleteMin();
	}

	/**
	 * public void decreaseKey(HeapNode x, int delta)
	 *
	 * Decreases the key of the node x by a non-negative value delta. The structure
	 * of the heap should be updated to reflect this change (for example, the
	 * cascading cuts procedure should be applied if needed).
	 * complexity of O(logn)
	 */
	public void decreaseKey(HeapNode x, int delta) {
		HeapNode parent = x.parent;

		if(x.key < 0 && delta == Integer.MAX_VALUE)
			x.key = Integer.MIN_VALUE;
		
		else
			x.key -= delta;
		
		if (parent != null && x.getKey() < parent.getKey()) {
			cascadingCut(x);
		}
		if (x.getKey() < min.getKey())
			min = x;
	}
	

	/**
	 * public void cascadingCut(HeapNode x)
	 *
	 * Help function to cut the node x from its parent
	 */
	private void cascadingCut(HeapNode x) {
		HeapNode parent = x.parent;
		cut(x);
    	if(parent != null && parent.parent != null)
    	{
    		if(!parent.mark)
    		{
    			parent.mark = true;
    			totalMarked ++;
    			return;
    		}
    		else
				cascadingCut(parent);
    	}
	}

	/**
	 * public void cut(HeapNode x)
	 *
	 * Help function to cut the node x from its parent
	 */
	private void cut(HeapNode x) {
		HeapNode parent = x.parent;

		if(x.next == x)
			parent.child = null;
		
		else
		{
			if(parent.child == x)
				parent.child = x.next;
			x.prev.next = x.next;
			x.next.prev = x.prev;
		}
		x.parent = null;
		parent.rank = parent.rank - 1;
		if (x.mark) {
			x.mark = false;
			totalMarked--;
		}
		insertNode(x);
		totalCuts++;
	}

	/**
	 * public int nonMarked()
	 *
	 * This function returns the current number of non-marked items in the heap
	 * complexity of O(1)
	 */
	public int nonMarked() {
		return size - totalMarked;
	}

	/**
	 * public int potential()
	 *
	 * This function returns the current potential of the heap, which is: Potential
	 * = #trees + 2*#marked
	 * 
	 * In words: The potential equals to the number of trees in the heap plus twice
	 * the number of marked nodes in the heap.
	 * 	 * complexity of O(1)
	 */
	public int potential() {
		return totalTrees + 2 * totalMarked;
	}

	/**
	 * public static int totalLinks()
	 *
	 * This static function returns the total number of link operations made during
	 * the run-time of the program. A link operation is the operation which gets as
	 * input two trees of the same rank, and generates a tree of rank bigger by one,
	 * by hanging the tree which has larger value in its root under the other tree.
	 * complexity of O(1)
	 */
	public static int totalLinks() {
		return totalLinks; // should be replaced by student code
	}

	/**
	 * public static int totalCuts()
	 *
	 * This static function returns the total number of cut operations made during
	 * the run-time of the program. A cut operation is the operation which
	 * disconnects a subtree from its parent (during decreaseKey/delete methods).
	 * complexity of O(1)
	 */
	public static int totalCuts() {
		return totalCuts;
	}

	/**
	 * public static int[] kMin(FibonacciHeap H, int k)
	 * This static function returns the k smallest elements in a Fibonacci heap that
	 * contains a single tree. The function should run in O(k*deg(H)). (deg(H) is
	 * the degree of the only tree in H.)
	 * complexity of O(k*deg(H))
	 * ###CRITICAL### : you are NOT allowed to change H.
	 */
	   public static int[] kMin(FibonacciHeap H, int k)
	    {
	        if(H.isEmpty() || k ==0){
	            int[] array = new int[0];
	            return array;
	        }

	        int[] minList = new int [k];
	        FibonacciHeap tempHeap = new FibonacciHeap();
	        HeapNode firstNode = tempHeap.insert(H.first.key);
	        firstNode.cloneNode(H.first);
	        
	        for(int i = 0; i < k; i++){
	            minList[i] = tempHeap.getMin();
	            HeapNode tempMin = tempHeap.findMin();
	            HeapNode child = tempMin.cloneMe.child;
	            tempHeap.deleteMin();

	            if (child != null) {
	            	firstNode = tempHeap.insert(child.key);
	            	firstNode.cloneMe = child;	            	
	            	HeapNode currNode = child;
	            	if(currNode.next != child)
	            	{
		            	do
			            {
			            	firstNode = tempHeap.insert(currNode.next.key);
			            	firstNode.cloneNode(currNode.next);
			            	currNode = currNode.next;
		
			            }while(currNode.next != child);
	            	}
	            }
	        }
	        return minList;
	    }
    
    
	/**
	 * public class HeapNode
	 * 
	 * If you wish to implement classes other than FibonacciHeap (for example
	 * HeapNode), do it in this file, not in another file.
	 * 
	 */
	public static class HeapNode {
        public HeapNode cloneMe;
		/** heap nodes class **/
		public int key;
		public int rank; // number of children in children's list
		public HeapNode parent;
		public HeapNode child; // A pointer to one of the children, doesn't matter who
		public HeapNode next;
		public HeapNode prev;
		public boolean mark; // If he already lost a child, mark as true otherwise, false
		
		public HeapNode(int key) {
			this.key = key;
			this.rank = 0; // At the beginning we add it with 0 children
			this.parent = null;
			this.child = null;
			this.next = this;
			this.prev = this;
			this.mark = false;
		}

		public int getKey() {
			return this.key;
		}
		
	    private void cloneNode(HeapNode node){
	    	this.cloneMe = node;
	    }
	}

}