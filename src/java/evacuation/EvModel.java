package evacuation;

import java.util.Random;

import pathFinding.*;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class EvModel extends GridWorldModel {

	public static final int DOOR = 32; // garbage code in grid model
	public static final int FIRE = 64;
	public static final int GSize = 26; // grid size
	//public boolean isOut = false;
	public Location ldoor1 = new Location(9, 0);
	public Location ldoor2 = new Location(16, 0);
	public Location ldoor3 = new Location(9, 25);
	public Location ldoor4 = new Location(16, 25);

	boolean nextSlot(int agId) throws Exception {

		Location il = getAgPos(agId);
		Random rnd = new Random();

		switch (rnd.nextInt(4)) {
		case 0:
			if (isFree(il.x - 1, il.y))
				setAgPos(agId, il.x - 1, il.y);
			break;
		case 1:
			if (isFree(il.x + 1, il.y))
				setAgPos(agId, il.x + 1, il.y);
			break;
		case 2:
			if (isFree(il.x, il.y - 1))
				setAgPos(agId, il.x, il.y - 1);
			break;
		case 3:
			if (isFree(il.x, il.y + 1))
				setAgPos(agId, il.x, il.y + 1);
			break;

		}
		return true;
	}

	boolean moveTowards(int x, int y, int agId) throws Exception {
		Location il = getAgPos(agId);

		if (il.x == x && il.y == y)
			return true;

		PathFinding pf = new PathFinding();
		pf.setReferencias(il.x, il.y);
		pf.setReferencias(x, y);

		Location next = pf.getNextStep();
		
		//in case of collision with another agent
		//agent moves to a random position !(x==next.x && y==next.y)
		if(!isFree(next) && !(new Location(x,y).equals(new Location(next.x,next.y)))) {
			nextSlot(agId);
			return true;
		}

		setAgPos(agId, next);

		return true;
	}

	EvModel() {
		// width, height and number of agents
		super(GSize, GSize, 17);
		// initial location of agents
		try {
			setAgPos(0, 0, 0);
			setAgPos(1, 0, 5);
			setAgPos(2, 0, 11);
			setAgPos(3, 0, 17);
			setAgPos(4, 0, 23);
			setAgPos(5, 25, 0);
			setAgPos(6, 25, 5);
			setAgPos(7, 25, 11);
			setAgPos(8, 25, 17);
			setAgPos(9, 25, 23);

			setAgPos(10, 25, 7);
			setAgPos(11, 0, 16);
			setAgPos(12, 9, 17);

			setAgPos(13, 9, 2);
			setAgPos(14, 16, 2);
			setAgPos(15, 9, 23);
			setAgPos(16, 16, 23);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// location of doors
		add(EvModel.DOOR, 9, 0);
		add(EvModel.DOOR, 16, 0);
		add(EvModel.DOOR, 9, 25);
		add(EvModel.DOOR, 16, 25);

		add(EvModel.OBSTACLE, 4, 0);
		// add(EvModel.OBSTACLE, 4, 1);
		add(EvModel.OBSTACLE, 4, 2);
		add(EvModel.OBSTACLE, 4, 3);
		add(EvModel.OBSTACLE, 4, 4);
		// add(EvModel.OBSTACLE, 4, 5);
		add(EvModel.OBSTACLE, 4, 6);
		add(EvModel.OBSTACLE, 4, 7);
		add(EvModel.OBSTACLE, 4, 8);
		add(EvModel.OBSTACLE, 4, 9);
		add(EvModel.OBSTACLE, 4, 10);
		// add(EvModel.OBSTACLE, 4, 11);
		add(EvModel.OBSTACLE, 4, 12);
		add(EvModel.OBSTACLE, 4, 13);
		add(EvModel.OBSTACLE, 4, 14);
		add(EvModel.OBSTACLE, 4, 15);
		add(EvModel.OBSTACLE, 4, 16);
		// add(EvModel.OBSTACLE, 4, 17);
		add(EvModel.OBSTACLE, 4, 18);
		add(EvModel.OBSTACLE, 4, 19);
		add(EvModel.OBSTACLE, 4, 20);
		add(EvModel.OBSTACLE, 4, 21);
		add(EvModel.OBSTACLE, 4, 22);
		// add(EvModel.OBSTACLE, 4, 23);
		add(EvModel.OBSTACLE, 4, 24);
		add(EvModel.OBSTACLE, 4, 25);

		add(EvModel.OBSTACLE, 7, 3);
		add(EvModel.OBSTACLE, 7, 4);
		add(EvModel.OBSTACLE, 7, 5);
		// add(EvModel.OBSTACLE, 7, 6);
		add(EvModel.OBSTACLE, 7, 7);
		add(EvModel.OBSTACLE, 7, 8);
		add(EvModel.OBSTACLE, 8, 8);
		add(EvModel.OBSTACLE, 9, 8);
		add(EvModel.OBSTACLE, 10, 8);

		add(EvModel.OBSTACLE, 7, 9);
		// add(EvModel.OBSTACLE, 7, 10);
		add(EvModel.OBSTACLE, 7, 11);
		add(EvModel.OBSTACLE, 7, 12);
		add(EvModel.OBSTACLE, 8, 12);
		add(EvModel.OBSTACLE, 9, 12);
		add(EvModel.OBSTACLE, 10, 12);

		add(EvModel.OBSTACLE, 7, 13);
		// add(EvModel.OBSTACLE, 7, 14);
		add(EvModel.OBSTACLE, 7, 15);
		add(EvModel.OBSTACLE, 7, 16);
		add(EvModel.OBSTACLE, 8, 16);
		add(EvModel.OBSTACLE, 9, 16);
		add(EvModel.OBSTACLE, 10, 16);

		add(EvModel.OBSTACLE, 7, 17);
		// add(EvModel.OBSTACLE, 7, 18);
		add(EvModel.OBSTACLE, 7, 19);
		add(EvModel.OBSTACLE, 7, 20);
		add(EvModel.OBSTACLE, 7, 21);

		add(EvModel.OBSTACLE, 21, 0);
		// add(EvModel.OBSTACLE, 21, 1);
		add(EvModel.OBSTACLE, 21, 2);
		add(EvModel.OBSTACLE, 21, 3);
		add(EvModel.OBSTACLE, 21, 4);
		// add(EvModel.OBSTACLE, 21, 5);
		add(EvModel.OBSTACLE, 21, 6);
		add(EvModel.OBSTACLE, 21, 7);
		add(EvModel.OBSTACLE, 21, 8);
		add(EvModel.OBSTACLE, 21, 9);
		add(EvModel.OBSTACLE, 21, 10);
		// add(EvModel.OBSTACLE, 21, 11);
		add(EvModel.OBSTACLE, 21, 12);
		add(EvModel.OBSTACLE, 21, 13);
		add(EvModel.OBSTACLE, 21, 14);
		add(EvModel.OBSTACLE, 21, 15);
		add(EvModel.OBSTACLE, 21, 16);
		// add(EvModel.OBSTACLE, 21, 17);
		add(EvModel.OBSTACLE, 21, 18);
		add(EvModel.OBSTACLE, 21, 19);
		add(EvModel.OBSTACLE, 21, 20);
		add(EvModel.OBSTACLE, 21, 21);
		add(EvModel.OBSTACLE, 21, 22);
		// add(EvModel.OBSTACLE, 21, 23);
		add(EvModel.OBSTACLE, 21, 24);
		add(EvModel.OBSTACLE, 21, 25);

		add(EvModel.OBSTACLE, 18, 3);
		add(EvModel.OBSTACLE, 18, 4);
		add(EvModel.OBSTACLE, 18, 5);
		// add(EvModel.OBSTACLE, 18, 6);
		add(EvModel.OBSTACLE, 18, 7);
		add(EvModel.OBSTACLE, 18, 8);
		add(EvModel.OBSTACLE, 17, 8);
		add(EvModel.OBSTACLE, 16, 8);
		add(EvModel.OBSTACLE, 15, 8);

		add(EvModel.OBSTACLE, 18, 9);
		// add(EvModel.OBSTACLE, 18, 10);
		add(EvModel.OBSTACLE, 18, 11);
		add(EvModel.OBSTACLE, 18, 12);
		add(EvModel.OBSTACLE, 17, 12);
		add(EvModel.OBSTACLE, 16, 12);
		add(EvModel.OBSTACLE, 15, 12);

		add(EvModel.OBSTACLE, 18, 13);
		// add(EvModel.OBSTACLE, 18, 14);
		add(EvModel.OBSTACLE, 18, 15);
		add(EvModel.OBSTACLE, 18, 16);
		add(EvModel.OBSTACLE, 17, 16);
		add(EvModel.OBSTACLE, 16, 16);
		add(EvModel.OBSTACLE, 15, 16);

		add(EvModel.OBSTACLE, 18, 17);
		// add(EvModel.OBSTACLE, 18, 18);
		add(EvModel.OBSTACLE, 18, 19);
		add(EvModel.OBSTACLE, 18, 20);
		add(EvModel.OBSTACLE, 18, 21);

		add(EvModel.OBSTACLE, 7, 22);
		add(EvModel.OBSTACLE, 8, 22);
		add(EvModel.OBSTACLE, 9, 22);
		add(EvModel.OBSTACLE, 10, 22);
		add(EvModel.OBSTACLE, 11, 22);
		add(EvModel.OBSTACLE, 12, 22);
		add(EvModel.OBSTACLE, 13, 22);
		add(EvModel.OBSTACLE, 14, 22);
		add(EvModel.OBSTACLE, 15, 22);
		add(EvModel.OBSTACLE, 16, 22);
		add(EvModel.OBSTACLE, 17, 22);
		add(EvModel.OBSTACLE, 18, 22);

		add(EvModel.OBSTACLE, 7, 3);
		add(EvModel.OBSTACLE, 8, 3);
		add(EvModel.OBSTACLE, 9, 3);
		add(EvModel.OBSTACLE, 10, 3);
		add(EvModel.OBSTACLE, 11, 3);
		add(EvModel.OBSTACLE, 12, 3);
		add(EvModel.OBSTACLE, 13, 3);
		add(EvModel.OBSTACLE, 14, 3);
		add(EvModel.OBSTACLE, 15, 3);
		add(EvModel.OBSTACLE, 16, 3);
		add(EvModel.OBSTACLE, 17, 3);

		add(EvModel.OBSTACLE, 11, 4);
		add(EvModel.OBSTACLE, 11, 5);
		add(EvModel.OBSTACLE, 11, 6);
		add(EvModel.OBSTACLE, 11, 7);
		add(EvModel.OBSTACLE, 11, 8);
		add(EvModel.OBSTACLE, 11, 9);
		add(EvModel.OBSTACLE, 11, 10);
		add(EvModel.OBSTACLE, 11, 11);
		add(EvModel.OBSTACLE, 11, 12);
		add(EvModel.OBSTACLE, 11, 13);
		add(EvModel.OBSTACLE, 11, 14);
		add(EvModel.OBSTACLE, 11, 15);
		add(EvModel.OBSTACLE, 11, 16);
		add(EvModel.OBSTACLE, 11, 17);
		add(EvModel.OBSTACLE, 11, 18);
		add(EvModel.OBSTACLE, 11, 19);
		add(EvModel.OBSTACLE, 11, 20);
		add(EvModel.OBSTACLE, 11, 21);

		add(EvModel.OBSTACLE, 14, 4);
		add(EvModel.OBSTACLE, 14, 5);
		add(EvModel.OBSTACLE, 14, 6);
		add(EvModel.OBSTACLE, 14, 7);
		add(EvModel.OBSTACLE, 14, 8);
		add(EvModel.OBSTACLE, 14, 9);
		add(EvModel.OBSTACLE, 14, 10);
		add(EvModel.OBSTACLE, 14, 11);
		add(EvModel.OBSTACLE, 14, 12);
		add(EvModel.OBSTACLE, 14, 13);
		add(EvModel.OBSTACLE, 14, 14);
		add(EvModel.OBSTACLE, 14, 15);
		add(EvModel.OBSTACLE, 14, 16);
		add(EvModel.OBSTACLE, 14, 17);
		add(EvModel.OBSTACLE, 14, 18);
		add(EvModel.OBSTACLE, 14, 19);
		add(EvModel.OBSTACLE, 14, 20);
		add(EvModel.OBSTACLE, 14, 21);

		add(EvModel.OBSTACLE, 0, 2);
		add(EvModel.OBSTACLE, 1, 2);
		add(EvModel.OBSTACLE, 2, 2);
		add(EvModel.OBSTACLE, 3, 2);

		add(EvModel.OBSTACLE, 0, 8);
		add(EvModel.OBSTACLE, 1, 8);
		add(EvModel.OBSTACLE, 2, 8);
		add(EvModel.OBSTACLE, 3, 8);

		add(EvModel.OBSTACLE, 0, 14);
		add(EvModel.OBSTACLE, 1, 14);
		add(EvModel.OBSTACLE, 2, 14);
		add(EvModel.OBSTACLE, 3, 14);

		add(EvModel.OBSTACLE, 0, 20);
		add(EvModel.OBSTACLE, 1, 20);
		add(EvModel.OBSTACLE, 2, 20);
		add(EvModel.OBSTACLE, 3, 20);

		add(EvModel.OBSTACLE, 22, 2);
		add(EvModel.OBSTACLE, 23, 2);
		add(EvModel.OBSTACLE, 24, 2);
		add(EvModel.OBSTACLE, 25, 2);

		add(EvModel.OBSTACLE, 22, 8);
		add(EvModel.OBSTACLE, 23, 8);
		add(EvModel.OBSTACLE, 24, 8);
		add(EvModel.OBSTACLE, 25, 8);

		add(EvModel.OBSTACLE, 22, 14);
		add(EvModel.OBSTACLE, 23, 14);
		add(EvModel.OBSTACLE, 24, 14);
		add(EvModel.OBSTACLE, 25, 14);

		add(EvModel.OBSTACLE, 22, 20);
		add(EvModel.OBSTACLE, 23, 20);
		add(EvModel.OBSTACLE, 24, 20);
		add(EvModel.OBSTACLE, 25, 20);

	}

}