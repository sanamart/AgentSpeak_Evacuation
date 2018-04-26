// Internal action code for project evacuation

package intactions;

import java.util.TreeMap;
import evacuation.EvEnv;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.grid.Location;

public class door extends DefaultInternalAction {

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] values) throws Exception {
		try {
			int iagx = (int) ((NumberTerm) values[0]).solve();
			int iagy = (int) ((NumberTerm) values[1]).solve();

			Location ag = new Location(iagx, iagy);
			TreeMap<Integer, Integer> myMap = new TreeMap<Integer, Integer>();
			myMap.put(ag.distance(EvEnv.model.ldoor1), 1);
			myMap.put(ag.distance(EvEnv.model.ldoor2), 2);
			myMap.put(ag.distance(EvEnv.model.ldoor3), 3);
			myMap.put(ag.distance(EvEnv.model.ldoor4), 4);

			int door = myMap.firstEntry().getValue();
			return un.unifies(values[2], new NumberTermImpl(door));
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}

	}
}
