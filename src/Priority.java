
//package assignment2;
//
//// Priority queue based on least time stamp = higher priority
//public class Priority<ElementType extends Comparable<ElementType>> {
//
//	private ElementType[] elements; // Array that holds priority queue elements
//	private int lastIndex; // index of last element in priority queue
//	private int maxIndex; // index of last position in array
//
//	public ElementType dequeue() { // TODO implement
//
//		ElementType hold = null; // item to deque
//		ElementType toMove;// item to move down
//
//		if (lastIndex == -1)
//			System.err.println("Priority queue is empty");
//		else {
//			hold = elements[0]; // remember item to be returned
//			toMove = elements[lastIndex]; // item to reheap down
//			lastIndex = lastIndex - 1; // decrease priority queue size
//			reheapDown(toMove); // rstore heap properties
//			// return largest element
//		}
//		return hold;
//
//	}
//
//	public void enqueue(ElementType event) { // TODO implement
//		if (lastIndex == maxIndex)
//			System.err.println("Priority queue is full");
//		else {
//			lastIndex = lastIndex + 1;
//			reheapUp(event);
//		}
//
//	}
//
//	private void reheapUp(ElementType item)
//	// Current lastIndex position is empty
//	// Inserts item into the tree and ensures shape and order properties
//	{
//		int lastElement = lastIndex;
//		while ((lastElement > 0) // hole is not root
//				&& // short circuit
//				(item.compareTo(elements[(lastElement - 1) / 2]) > 0)) // item >
//																		// hole's
//		// parent
//		{
//			elements[lastElement] = elements[(lastElement - 1) / 2]; // move
//																		// hole's
//																		// parent
//			// down
//			lastElement = (lastElement - 1) / 2; // move hole up
//		}
//
//		elements[lastElement] = item; // place item into final hole
//	}
//
//	private void reheapDown(ElementType item)
//	// Current root position is "empty";
//	// Inserts item into the tree and ensures shape and order properties
//	{
//		int hole = 0; // current index of hole
//		int newhole; // index where hole should move to
//
//		newhole = newElement(hole, item); // find next hole
//		while (newhole != hole) {
//			elements[hole] = elements[newhole]; // move element up
//			hole = newhole; // move hole down
//			newhole = newElement(hole, item); // find next hole
//		}
//		elements[hole] = item; // fill in the final hole
//	}
//
//	private int newElement(int element, ElementType item)
//	// If either child of hole is larger than item this returns the index
//	// of the larger child; otherwise it returns the index of hole
//	{
//		int left = (element * 2) + 1;
//		int right = (element * 2) + 2;
//
//		if (left > lastIndex)
//			// hole has no children
//			return element;
//		else if (left == lastIndex)
//			// hole has left child only
//			if (item.compareTo(elements[left]) < 0)
//				// item < left child
//				return left;
//			else
//				// item >= left child
//				return element;
//		else
//		// hole has two children
//		if (elements[left].compareTo(elements[right]) < 0)
//			// left child < right child
//			if (elements[right].compareTo(item) <= 0)
//				// right child <= item
//				return element;
//			else
//				// item < right child
//				return right;
//		else
//		// left child >= right child
//		if (elements[left].compareTo(item) <= 0)
//			// left child <= item
//			return element;
//		else
//			// item < left child
//			return left;
//	}
//
//	public boolean isEmpty()
//	// Determines whether this priority queue is empty.
//	{
//		return (lastIndex == -1);
//	}
//
//	public boolean isFull()
//	// Determines whether this priority queue is full.
//	{
//		return (lastIndex == maxIndex);
//	}
//
//	public String toString()
//	// returns a string of all the heap elements
//	{
//		String theHeap = new String("the heap is:\n");
//		for (int index = 0; index <= lastIndex; index++)
//			theHeap = theHeap + index + ". " + elements[index] + "\n";
//		return theHeap;
//	}
//	
//	public void remove() {
//		System.err.println("Element removed");
//
//	}
//
//	public static void main(String a[]) {
//		EventQueue<Integer> queue = new EventQueue<Integer>();
//		queue.enqueue(1);
//		queue.dequeue();
////		queue.enqueue(56);
////		queue.enqueue(2);
////		queue.enqueue(67);
////		queue.dequeue();
////		queue.dequeue();
////		queue.enqueue(24);
////		queue.dequeue();
////		queue.enqueue(98);
////		queue.enqueue(45);
////		queue.enqueue(23);
////		queue.enqueue(435);
//	}
//
//}
