package pathFinding;

import jason.environment.grid.Location;

public class Node {

	public int x, y;
	public int gCost, hCost, fCost;
	public Node parent, son, next;
	boolean visited;

	public Node(Location l) {
		x = l.x;
		y = l.y;
		visited = false;
		parent = null;
	}

	public Node(int _x, int _y) {
		x = _x;
		y = _y;
		visited = false;
		parent = null;
	}

}