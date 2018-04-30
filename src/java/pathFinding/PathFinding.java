package pathFinding;

import java.util.*;

import pathFinding.Node;
import jason.environment.grid.Location;
import evacuation.*;

public class PathFinding {
	EvModel mod = EvEnv.get();
	ArrayList<Node> my = new ArrayList<Node>();
	int x, y;
	int size;
	Node start, target;
	List open;
	List closed;
	List path;

	/**
	 * Constructor Initializes the start and target
	 * Nodes, and the 3 relevant
	 * lists for the algorithm
	 */
	public PathFinding() {
		start = null;
		target = null;
		open = new List();
		closed = new List();
		path = new List();
	}

	/**
	 * calls the method to initialize the 
	 * algorithm and return the next step 
	 * (x,y) to reached by an agent
	 * @return x and y coordinates
	 */
	public Location getNextStep() {
		iniciaAlgoritmo(start);

		Node extra2 = path.first;
		ArrayList<Node> List = new ArrayList<Node>();
		while (extra2 != null) {
			extra2.next = null;
			List.add(extra2);
			extra2 = extra2.parent;
		}

		Collections.reverse(List);
		Location next = new Location(List.get(1).x, List.get(1).y);

		return next;
	}

	/**
	 * initializes the algorithm
	 * 
	 * @param first
	 */
	public void iniciaAlgoritmo(Node first) {
		open.add(first);
		Node actual = open.deleteLessCost();

		while (!inOpenedList(target)) {
			Node extra = actual;
			extra.next = null;
			closed.add(extra);

			// checks wether the target is the actual node
			if (((actual.x == target.x) && (actual.y == target.y)))
				break; // if it is the case it ends

			else {
				// get the neighbour nodes of the actual node
				int x = 0, y = 0, w = 0, w2 = 0;
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {

						if (i == 1 && j == 1) {
							continue;
						}

						if (i == 0)
							w = -1;
						else if (i == 1)
							w = 0;
						else if (i == 2)
							w = 1;

						if (j == 0)
							w2 = -1;
						else if (j == 1)
							w2 = 0;
						else if (j == 2)
							w2 = 1;

						x = actual.x + w;
						y = actual.y + w2;

						// creates one of the neighbors
						Node neighbor = new Node(x, y);
						
						// checks if the node is inside the Grid, if it's a free
						// position (no obstacles or fire) or if it's already in
						// the closed list
						if ((!(mod.inGrid(neighbor.x, neighbor.y)
								&& (mod.isFreeOfObstacle(neighbor.x, neighbor.y)) && !mod
									.hasObject(EvModel.FIRE, neighbor.x,
											neighbor.y)))
								|| inClosed(neighbor)) {
							continue;// lo ignoro
						}

						// checks if the node is not in the opened and closed
						// lists
						// and adds it to the opened list
						if (!inOpenedList(neighbor) && !inClosed(neighbor)) {
							neighbor = setValues(actual, neighbor);
							neighbor.parent = actual;
							open.add(neighbor);
						}

						else if (inOpenedList(neighbor)) {
							if (setValues(actual, neighbor).fCost < actual.fCost) {
								neighbor = setValues(actual, neighbor);
								neighbor.parent = actual;
							}
						}

						else if (!inOpenedList(neighbor)) {
							if (inClosed(neighbor))
								;// lo ignoramos
						}

					}
				}

			}
			actual = open.deleteLessCost();
			// preguntamos si ya encontro el path para mostrarlo
			if ((actual.x == target.x) && (actual.y == target.y)) {
				Node extra2 = actual;
				while (extra2 != null) {
					extra2.next = null;
					path.add(extra2);
					extra2 = extra2.parent;
				}
			}
		}
	}

	int getDistance(Node nodeA, Node nodeB) {
		int distX = Math.abs(nodeA.x - nodeB.x);
		int distY = Math.abs(nodeA.y - nodeB.y);

		if (distX > distY)
			return 14 * distY + 10 * (distX - distY);
		return 14 * distX + 10 * (distY - distX);
	}

	/**
	 * Checks if a given node is in the opened list
	 * 
	 * @param nodin
	 * @return
	 */
	public boolean inOpenedList(Node nodin) {
		if (!open.isEmpty())
			return false;
		Node aux = open.first;

		if (aux == null)
			return false;

		while (aux != null) {
			if ((nodin.x == aux.x) && (nodin.y == aux.y))
				return true;

			aux = aux.next;
		}
		return false;

	}

	/**
	 * checks if a given node is in the closed list
	 * 
	 * @param nodin
	 * @return
	 */
	public boolean inClosed(Node nodin) {
		Node aux = closed.first;

		if (aux == null)
			return false;

		while (aux != null) {
			if ((nodin.x == aux.x) && (nodin.y == aux.y))
				return true;

			aux = aux.next;
		}
		return nodin.visited;
	}

	/**
	 * set the F, G and H values to the neighbor node
	 * 
	 * @param actual
	 * @param neighbor
	 * @return
	 */
	public Node setValues(Node actual, Node neighbor) {
		
		if ((actual.x == neighbor.x) && (actual.y == neighbor.y))
			return actual;
		else if ((actual.x == neighbor.x) || (actual.y == neighbor.y))
			neighbor.gCost = 10;
		else
			neighbor.gCost = 14;

		neighbor.hCost = getDistance(neighbor, target);
		neighbor.fCost = neighbor.gCost + neighbor.hCost;

		return neighbor;
	}

	/**
	 * set the start and target node
	 * 
	 * @param x
	 * @param y
	 */
	public void setReferencias(int x, int y) {
		if (start == null) {
			start = new Node(x, y);
		} else if (target == null) {
			target = new Node(x, y);
		}
	}

}
