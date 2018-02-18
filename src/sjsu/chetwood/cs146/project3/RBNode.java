package sjsu.chetwood.cs146.project3;

/**
 * 
 * @author rileychetwood
 *
 * @param <T>
 */
public class RBNode<T extends Comparable<T>> implements Comparable<RBNode<T>> {

	private T datum;
	private RBNode<T> parent;
	private RBNode<T> left;
	private RBNode<T> right;
	private boolean isRed;
	
	public RBNode(T datum) {
		this.datum = datum;
		isRed = true;
	}
	
	public T getDatum() {
		return datum;
	}

	public RBNode<T> getParent() {
		return parent;
	}

	public void setParent(RBNode<T> parent) {
		this.parent = parent;
	}
	
	public RBNode<T> getGrand() {
		return parent.getParent();
	}

	public RBNode<T> getLeft() {
		return left;
	}

	public void setLeft(RBNode<T> left) {
		this.left = left;
	}

	public RBNode<T> getRight() {
		return right;
	}

	public void setRight(RBNode<T> right) {
		this.right = right;
	}

	public void setRed() {
		this.isRed = true;
	}
	
	public boolean isRed() {
		return isRed;
	}
	
	public void setBlack() {
		this.isRed = false;
	}
	
	public boolean isBlack() {
		return !(isRed);
	}
	
	public int compareTo(RBNode<T> o) {
		return datum.compareTo(o.getDatum());
	}
	
	@Override
	public String toString() {
		return (String) datum;
	}
	
}
