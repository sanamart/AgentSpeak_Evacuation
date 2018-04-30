package pathFinding;

public class List {
	public Node first, last, current;

	/**
	 * Constructor
	 */
	public List() {
		this.first = this.last = null;
	}

	/**
	 * To add a node at the end of the list
	 * 
	 * @param nodin
	 */
	public void add(Node nodin) {

		if (first == null) {
			first = last = nodin;
		}

		else {
			current = first;
			while (current.next != null) {
				current = current.next;
			}
			current.next = nodin;
			last = current.next;
		}
	}

	/**
	 * Deletes the first node of the list and returns it.
	 * 
	 * @return Node
	 */
	public Node deleteFirst() {
		Node temp = first;
		first = first.next;
		return temp;
	}

	/**
	 * deletes the node received as parameter and returns it
	 * 
	 * @param nodin
	 * @return Node
	 */
	public Node delete(Node nodin) {
		Node current = first;
		if ((nodin.x == first.x) && (nodin.y == first.y)) {
			return deleteFirst();
		}
		while (current.next != null) {
			if ((nodin.x == current.next.x) && (nodin.y == current.next.y)) {
				current.next = current.next.next;
			} else
				current = current.next;
		}
		return nodin;
	}

	/**
	 * deletes the node with less cost
	 * 
	 * @return Node
	 */
	public Node deleteLessCost() {
		Node current = first;
		Node menor = first;

		while (current != null) {
			if (current.fCost < menor.fCost)
				menor = current;
			else
				current = current.next;
		}
		return delete(menor);
	}

	/**
	 * checks whether the list is empty
	 * 
	 * @return true or false
	 */
	public boolean isEmpty() {
		return first == null;
	}
}
