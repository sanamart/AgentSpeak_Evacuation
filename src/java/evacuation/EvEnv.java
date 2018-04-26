package evacuation;

import java.util.logging.Logger;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;
import java.util.logging.Level;

public class EvEnv extends Environment {
	static Logger logger = Logger.getLogger(EvEnv.class.getName());

	public static EvModel model;
	EvView view;

	public static int sleep = 350;

	public static final Term ns = Literal.parseLiteral("next(slot)");
	public static final Literal al = Literal.parseLiteral("alarm");
	public static final Literal rad = Literal.parseLiteral("scape(rescuer,door)");
	public static final Literal iad = Literal.parseLiteral("ata(independant,door)");

	static boolean fire = false;

	/** Called before the MAS execution evacuation.mas2j */
	@Override
	public void init(String[] args) {
		model = new EvModel();
		view = new EvView(model);
		model.setView(view);
		clearPercepts();
		
		//Global Percepts (location of the doors)
		addPercept(Literal.parseLiteral("pos(1," + model.ldoor1.x + ","
				+ model.ldoor1.y + ")"));
		addPercept(Literal.parseLiteral("pos(2," + model.ldoor2.x + ","
				+ model.ldoor2.y + ")"));
		addPercept(Literal.parseLiteral("pos(3," + model.ldoor3.x + ","
				+ model.ldoor3.y + ")"));
		addPercept(Literal.parseLiteral("pos(4," + model.ldoor4.x + ","
				+ model.ldoor4.y + ")"));
		updateAgsPercept();
		informAgsEnvironmentChanged();
	}

	public static EvModel get() {
		return model;
	}

	@Override
	public boolean executeAction(String ag, Structure action) {
		boolean result = false;

		try {
			if (sleep > 0) {
				Thread.sleep(sleep);
			}

			int agId = getAgIdBasedOnName(ag);

			if (action.equals(ns)) {
				result = model.nextSlot(agId);
			} else if (action.getFunctor().equals("move_towards")) {
				int x = (int) ((NumberTerm) action.getTerm(0)).solve();
				int y = (int) ((NumberTerm) action.getTerm(1)).solve();
				result = model.moveTowards(x, y, agId);

			} else {
				logger.info("executing: " + action + ", but not implemented!");
			}
			if (result) {
				//update de percepts of agent "agId"
				updateAgPercept(agId);
				return true;
			}
		} catch (InterruptedException e) {
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"error executing " + action + " for " + ag, e);
		}
		return false;

	}

	private int getAgIdBasedOnName(String agName) {
		int id = -1;
		if (agName.substring(0, 3).equals("ind")) {
			id = (Integer.parseInt(agName.substring(11))) - 1;
		} else if (agName.substring(0, 3).equals("inj")) {
			id = (Integer.parseInt(agName.substring(7))) + 9;
		} else if (agName.substring(0, 3).equals("res")) {
			id = (Integer.parseInt(agName.substring(7))) + 12;
		}
		return id;
	}

	private void updateAgsPercept() {
		for (int i = 0; i < model.getNbOfAgs(); i++) {
			updateAgPercept(i);
		}
	}

	private void updateAgPercept(int ag) {
		if (ag < 10) {
			updateAgPercept("independant" + (ag + 1), ag);
		} else if (ag > 9 && ag < 13) {
			updateAgPercept("injured" + (ag - 9), ag);
		} else {
			updateAgPercept("rescuer" + (ag - 12), ag);
		}
	}

	/** creates the agents perception based on the EvModel */
	void updateAgPercept(String agName, int ag) {

		clearPercepts(agName);

		Location l = model.getAgPos(ag);

		//update the position of agent "ag"
		Literal pos1 = Literal.parseLiteral("pos(" + agName + "," + l.x + ","
				+ l.y + ")");

		addPercept(pos1);
		if (model.hasObject(model.DOOR, l)) {
			if (ag >= 12) {
				addPercept(agName, rad);
			} else {
				addPercept(agName, iad);
			}
		}

		if (fire)
			addPercept(al);
	}
}