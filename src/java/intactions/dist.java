// Internal action code for project evacuation

package intactions;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;
import jason.environment.grid.Location;

public class dist extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] values) throws Exception {
        try {        	
            int iagx = (int) ((NumberTerm) values[0]).solve();
            int iagy = (int) ((NumberTerm) values[1]).solve();
            int itox = (int) ((NumberTerm) values[2]).solve();
            int itoy = (int) ((NumberTerm) values[3]).solve();
            int dist = new Location(iagx, iagy).distance(new Location(itox, itoy));
            return un.unifies(values[4], new NumberTermImpl(dist));
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
    }
}
