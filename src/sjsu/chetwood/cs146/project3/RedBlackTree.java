package sjsu.chetwood.cs146.project3;

/**
 * RedBlackTree is a class that ensures that its tree 
 * is kept balanced. This guarantees that most tree operations
 * run with a time complexity of O(lgn). The height of the 
 * longest branch will never be more than one level higher than the 
 * rest of the tree.
 * 
 * @author rileychetwood
 *
 * @param <E> the type of the element being inserted into the tree
 */
public class RedBlackTree<E extends Comparable<E>> {
	private RBNode<E> root;

	/**
	 * Ctor for an object of the class RedBlackTree
	 * 
	 * PRECONDITION: None.
	 * POSTCONDITION: An empty RedBlackTree is initialized . 
	 */
	public RedBlackTree() {
		root = null;
	}

	/**
	 * Inserts an element into this RedBlackTree, first by
	 * locating the element's proper position using its compareTo
	 * method and the BST property, then adjusting the elements 
	 * to ensure the RedBlackTree properties are met. NOTE: Insertion 
	 * is delegated to a private function.
	 * 
	 * PRECONDITION: insertThis is not null.
	 * POSTCONDITION: this RedBlackTree is on element larger and 
	 * insertThis is in its sorted position.
	 * 
	 * @param insertThis E					the datum to be inserted as a RBNode into tree
	 * @throws IllegalArgumentException		if insertThis is null throw
	 */
	public void insert(E insertThis) throws IllegalArgumentException {
		if(insertThis == null) { 
			throw new IllegalArgumentException("ERROR: null value attempting to be inserted."); 
		}

		// passes packaged datum to delegate insertion
		insert(new RBNode<>(insertThis));								
	}

	/**
	 * Inserts an element into this RedBlackTree, first by
	 * locating the element's proper position using its compareTo
	 * method and the BST property, then adjusting the elements 
	 * to ensure the RedBlackTree properties are met.
	 * 
	 * PRECONDITIONS: None. Already addressed in the public insert method.
	 * POSTCONDITIONS: This RedBlackTree is on element larger and 
	 * insertThis is in its sorted position
	 * 
	 * @param insertThis RBNode<E>		the RBNode to be inserted into the tree. 
	 * 									Assumed to be red.
	 */
	private void insert(RBNode<E> insertThis) {

		// traversingNode is a dummy RBNode that traverses the tree to 
		// find proper position for insertThis. previous references the node 
		// previously referenced by traversingNode
		RBNode<E> traversingNode = root;	
		RBNode<E> previous = null; 	

		// traverse tree going left or right until insertThis's position is found 
		while(traversingNode != null) {

			// at the end of the loop previous will be insertThis's parent
			previous = traversingNode;

			// insertThis is less than the current value in traversingNode go to left subtree
			if(insertThis.compareTo(traversingNode) <= -1) {
				traversingNode = traversingNode.getLeft();
			} 

			// insertThis is greater than the current value in traversingNode go to right subtree
			else {
				traversingNode = traversingNode.getRight();
			}
		}

		// previous is now insertThis's parent. Set insertThis's parent to previous.
		insertThis.setParent(previous);

		// Empty tree. insertThis is the root
		if(previous == null) {
			root = insertThis;
		}

		// insertThis is less than its parent. Set previous's (insertThis's parent) left 
		// child to insertThis
		else if(insertThis.compareTo(previous) <= -1) {
			previous.setLeft(insertThis);
		}
		// insertThis is greater than its parent. Set previous's (insertThis's parent) 
		// child to insertThis
		else {
			previous.setRight(insertThis);
		}

		// set both of insertThis's children to null and set its color to red
		insertThis.setLeft(null);
		insertThis.setRight(null);
		insertThis.setRed();

		// make sure red black tree properties are met
		fixTree(insertThis);
	}

	/**
	 * Ensures the following properties are met within the red black tree:
	 * 
	 *  	1. Every node is either red or black.
	 * 	2. The root is black.
	 *	3. Every leaf (null) is black.
	 *	4. If a node is red, then both its children are black.
	 *	5. For each node, all simple paths from the node to descendant 
	 *	leaves contain the same number of black nodes.
	 * 
	 * PRECONDITION: None. Already addressed in the public insert method.
	 * POSTCONDITION: The tree has height to greater than lgn.
	 * 
	 * @param insertThis RBNode<E>	the node inserted into the tree with the 
	 * 								color red
	 */
	private void fixTree(RBNode<E> insertThis) {

		// insertThis's aunt (i.e. insertThis.p.p.(r/l))
		RBNode<E> aunt = null;

		while(insertThis.getParent() != null && insertThis.getParent().isRed()) {

			// insertThis is in its grandparent's left subtree 
			if(insertThis.getParent() == insertThis.getGrand().getLeft()) {
				aunt = insertThis.getGrand().getRight();

				// case 1: aunt is red and insertThis is red (violates rule 4
				// stated in method commenting), so make aunt and parent black
				// and grandparent black 
				if(aunt != null && aunt.isRed()) {								
					insertThis.getParent().setBlack();
					aunt.setBlack();
					insertThis.getGrand().setRed();
					insertThis = insertThis.getGrand();
				}

				// case 2: insertThis's aunt is black and insertThis is a right child
				else if(insertThis == insertThis.getParent().getRight()) {
					insertThis = insertThis.getParent();
					rotateLeft(insertThis); 
				}

				// case 3: insertThis's aunt is black and insertThis is a left child
				else {
					insertThis.getParent().setBlack();
					insertThis.getGrand().setRed();
					rotateRight(insertThis.getGrand());
				}
			}

			// inserThis is in its grandparent's right subtree 
			else {
				aunt = insertThis.getGrand().getLeft();

				// case 1: aunt is red and insertThis is red (violates rule 4
				// stated in method commenting), so make aunt and parent black
				// and grandparent black 
				if(aunt != null && aunt.isRed()) {
					insertThis.getParent().setBlack();
					aunt.setBlack();
					insertThis.getGrand().setRed();
					insertThis = insertThis.getGrand();
				}

				// case 2: insertThis's aunt is black and insertThis is a right child
				else if(insertThis == insertThis.getParent().getLeft()) {
					insertThis = insertThis.getParent();
					rotateRight(insertThis); 
				}

				// case 3: insertThis's aunt is black and insertThis is a left child
				else {
					insertThis.getParent().setBlack();
					insertThis.getGrand().setRed();
					rotateLeft(insertThis.getGrand());
				}
			}		
		}

		// rule 2: root must be black
		root.setBlack();
	}

	private void rotateLeft(RBNode<E> aroundThis) {

		RBNode<E> y = aroundThis.getRight();

		aroundThis.setRight(y.getLeft());

		if(y.getLeft() != null) {
			y.getLeft().setParent(aroundThis);
		}

		y.setParent(aroundThis.getParent());

		if(aroundThis.getParent() == null) {
			this.root = y;
		}

		else if(aroundThis == aroundThis.getParent().getLeft()) {
			aroundThis.getParent().setLeft(y);
		}

		else {
			aroundThis.getParent().setRight(y);
		}

		y.setLeft(aroundThis);
		aroundThis.setParent(y);
	}

	private void rotateRight(RBNode<E> aroundThis) {

		RBNode<E> x = aroundThis.getLeft();

		aroundThis.setLeft(x.getRight());

		if(x.getRight() != null) {
			x.getRight().setParent(aroundThis);
		}

		x.setParent(aroundThis.getParent());

		if(aroundThis.getParent() == null) {
			this.root = x;
		}

		else if(aroundThis == aroundThis.getParent().getRight()) {
			aroundThis.getParent().setRight(x);
		}

		else {
			aroundThis.getParent().setLeft(x);
		}
		x.setRight(aroundThis);
		aroundThis.setParent(x);
	}

	/**
	 * Searches the list of elements, called list, for a key.
	 * Note: key and element are the same in this case.
	 * 
	 * PRECONDITION: list and key are not null 
	 * POSTCONDITION: a reference to the element matching the key
	 * 
	 * @param root MyList<E> 			the list of elements 	
	 * @param key E 						the key to search for		
	 * @return RBNode<E>					the BTNode matching the key
	 */
	private RBNode<E> search(RBNode<E> root, E key) {
		if(root == null || root.getDatum().compareTo(key) == 0) {
			return root;
		}
		if(key.compareTo(root.getDatum()) <= -1) {
			return search(root.getLeft(), key);
		}

		return search(root.getRight(), key);
	}

	public RBNode<E> search(E key) {
		return search(root, key);
	}

	/**
	 * Gets the minimum value from the list
	 * 
	 * PRECONDITION: list is not null
	 * POSTCONDITION: none
	 * 
	 * @param list MyList<E> 		the list to search for the minimum
	 * @return RBNode<E>				the minimum element in the list
	 */
	private RBNode<E> minimum(RBNode<E> list) {
		RBNode<E> currentNode = list;
		while(currentNode.getLeft() != null) {
			currentNode = currentNode.getLeft();
		}
		return currentNode;
	}

	public RBNode<E> minimum() {
		return minimum(root);
	}

	/**
	 * Gets the maximum value from the list
	 * 
	 * PRECONDITION: list is not null
	 * POSTCONDITION: none
	 * 
	 * @param list MyList<E> 		the list to search for the maximum
	 * @return RBNode<E>				the maximum value in the list
	 */
	private RBNode<E> maximum(RBNode<E> list) {
		RBNode<E> currentNode = list;
		while(currentNode.getRight() != null) {
			currentNode = currentNode.getRight();
		}
		return currentNode;
	}

	public RBNode<E> maximum() {
		return maximum(root);
	}

	/**
	 * Gets the element after successorToThis
	 * 
	 * PRECONDITION: list is not null
	 * POSTCONDITION: none
	 * 
	 * @param successorToThis RBNode<E>		the element whose successor to find
	 * @return RBNode<E>						the successor to the element suuccessorToThis	
	 */
	public RBNode<E> successor(RBNode<E> successorToThis) {
		RBNode<E> successor = null;
		if(successorToThis.getRight() != null) {
			return minimum(successorToThis.getRight());
		}
		successor = successorToThis.getParent();
		while(successor != null && successorToThis == successor.getRight()) {
			successorToThis = successor;
			successor = successor.getParent();
		}
		return successor;
	}

	/**
	 * Prints the element's of a tree in its natural order
	 * 
	 * PRECONDITION: root is not null
	 * POSTCONDITION: none
	 * 
	 * @param root RBNode<E> 	the root of the tree
	 */
	private void inOrder(RBNode<E> root) {
		if(root != null) {
			inOrder(root.getLeft());
			if(this.root == root) 
				System.out.print("(" + root + ") ");
			else
				System.out.print(root + " ");
			inOrder(root.getRight());
		}
	}

	public void inOrder() {
		inOrder(root);
	}

	// add this in your class 
	// required by provided tester
	//************************************************************** 
	public static interface Visitor<E extends Comparable<E>>
	{
		/**
		     This method is called at each node.
		     @param n the visited node
		 */
		void visit(RBNode<E> n);
	}


	public void preOrderVisit(Visitor<E> v)
	{
		preOrderVisit(root, v);
	}

	private void preOrderVisit(RBNode<E> n, Visitor<E> v)
	{
		if (n == null) return;
		v.visit(n);
		preOrderVisit(n.getLeft(), v);
		preOrderVisit(n.getRight(), v);
	}
	//**************************************************************

	public static void main(String[] args) {
		RedBlackTree<String> rbt = new RedBlackTree<>();
		rbt.insert("D");
		rbt.insert("B");
		rbt.insert("A");
		rbt.insert("C");
		rbt.insert("F");
		rbt.insert("E");
		rbt.insert("H");
		rbt.insert("G");
		rbt.insert("I");
		rbt.insert("J");

		//		for(int i = 0; i < 20; i++) {
		//			if(i%2 == 0) {
		//				rbt.insert(i);
		//			}
		//		}

		rbt.inOrder();



	}
}
