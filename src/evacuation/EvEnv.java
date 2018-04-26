package de.hsh.bachelorarbeit.evacuation;


// Environment code for project evacuation
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import java.util.logging.Logger;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;
import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EvEnv extends Environment {
    static Logger logger = Logger.getLogger(EvEnv.class.getName());

	EvModel model;
    EvView  view;
    
    int     sleep    = 500;
    
    public static final Term    ns = Literal.parseLiteral("next(slot)");
    public static final Term    dg = Literal.parseLiteral("drop(garb)");
    public static final Term    bg = Literal.parseLiteral("burn(garb)");
    public static final Literal al = Literal.parseLiteral("alarm(independant)");
    public static final Literal ai = Literal.parseLiteral("at(rescuer,injured)");
    public static final Literal rad = Literal.parseLiteral("at(rescuer,door)");
    public static final Literal iad = Literal.parseLiteral("at(independant,door)");
    
    static boolean fire = false;
    
    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
        model = new EvModel();
        view  = new EvView(model);
        model.setView(view);
        updateAgsPercept();
        informAgsEnvironmentChanged();
    }
    

    @Override
    public boolean executeAction(String ag, Structure action) {
        logger.info(ag+" doing: "+ action);
        boolean result = false;
        
        
        try {
        	if (sleep > 0) {
                Thread.sleep(sleep);
            }

        	int agId = getAgIdBasedOnName(ag);
            
        	if (action.equals(ns)) {
                result = model.nextSlot(agId);
            }else if (action.getFunctor().equals("move_towards")) {
                int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                result = model.moveTowards(x,y,agId);
            
            }else {
            	logger.info("executing: " + action + ", but not implemented!");
            }
        	if (result) {
                updateAgPercept(agId);
                return true;
            }
        } catch (InterruptedException e) {
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error executing " + action + " for " + ag, e);
        }
        return false;
        
    }

    private int getAgIdBasedOnName(String agName) {
    	int id=-1;
    	if(agName.substring(0, 3).equals("ind")) {
    		id = (Integer.parseInt(agName.substring(11))) - 1;
    	}else if(agName.substring(0, 3).equals("inj")) {
    		id = 3;//(Integer.parseInt(agName.substring(7))) + 2;
    	}else if(agName.substring(0, 3).equals("res")){
    		id = 4;
    	}
        return id;
    }
    
    private void updateAgsPercept() {
        for (int i = 0; i < model.getNbOfAgs(); i++) {
            updateAgPercept(i);
        }
    }
    
    private void updateAgPercept(int ag) {
    	if(ag<3) {
        updateAgPercept("independant" + (ag + 1), ag);
    	}else if(ag==3) {
    		updateAgPercept("injured" , ag);
    	}else {
    		updateAgPercept("rescuer" , ag);
    	}
    }
    
    /** creates the agents perception based on the EvModel */
    void updateAgPercept(String agName, int ag) {
        clearPercepts(agName);

        Location l = model.getAgPos(ag);
        Location door = model.ldoor;

        
        Literal pos1 = Literal.parseLiteral("pos("+agName+"," + l.x + "," + l.y + ")");
        addPercept(pos1);
        Literal door_pos = Literal.parseLiteral("pos(door," + door.x + "," + door.y + ")");

        addPercept(door_pos);
        
        if (l.equals(model.ldoor)) {
        	if(ag==4) {
            addPercept(agName, rad);
        	}else {
        		addPercept(agName,iad);
        	}
        }
        
        if(ag==4) {
        if (l.equals(model.inj)) {
            addPercept("rescuer", ai);
        }
        
        }
        
        if(fire) {
        	addPercept(al);
        	
        	
        }

        /*if (model.hasObject(EvModel.FIRE, independantLoc)) {
            addPercept(al);
        }
        if (model.hasObject(INJURED, r2Loc)) {
            addPercept(g2);
        }*/
    }
    
    
    class EvModel extends GridWorldModel {

        public static final int DOOR  = 32; // garbage code in grid model
    	public static final int GSize = 8; // grid size
        public static final int MErr = 2; // max error in pick garb
        int nerr; // number of tries of pick garb
        boolean helperHasInjured = false; // whether helfer is carrying an injured or not
        public static final int   FIRE  = 64;
        Location ldoor = new Location(GSize-1, 3);
        Location inj = new Location(2,6);
        
        Random random = new Random(System.currentTimeMillis());

        private EvModel() {
        	//width, height and number of agents
            super(GSize, GSize, 5);
            // initial location of agents
            try {
                setAgPos(0,0,0);
                setAgPos(1,2,0);
                setAgPos(2,2,1);
                setAgPos(3,2,6);
                //setAgPos(4,3,2);
                setAgPos(4, GSize-2, 3);
               

                /*Location r2Loc = new Location(GSize/2, GSize/2);
                setAgPos(1, r2Loc);*/
            } catch (Exception e) {
                e.printStackTrace();
            }

            // initial location of garbage
            /*add(GARB, 3, 0);
            add(GARB, GSize-1, 0);
            add(GARB, 1, 2);
            add(GARB, 0, GSize-2);*/
            //add(FIRE, GSize-1, GSize-1);
            /*add(EvModel.OBSTACLE, 5, 0);
            add(EvModel.OBSTACLE, 5, 1);
            add(EvModel.OBSTACLE, 5, 2);
            add(EvModel.OBSTACLE, 5, 4);
            add(EvModel.OBSTACLE, 5, 5);
            add(EvModel.OBSTACLE, 5, 6);
            add(EvModel.OBSTACLE, 5, 7);*/
            add(DOOR, GSize-1, 3);
        }

        boolean nextSlot(int agId) throws Exception {
        	
        	Location il = getAgPos(agId);
        	Random rnd = new Random();
        	
        	switch (rnd.nextInt(4)) {
            case 0:
                if(isFree(il.x-1, il.y))
                	il.x--;
                break;
            case 1:
            	if(isFree(il.x+1, il.y))
                	il.x++;
                break;
            case 2:
            	if(isFree(il.x, il.y-1))
                	il.y--;
                break;
            case 3:
            	if(isFree(il.x, il.y+1))
                	il.y++;
                break;
        	
        	}
        	if(agId==3) {
                System.out.println("x:"+il.x+" y:"+il.y);
                inj = new Location(il.x,il.y);
        	}
                setAgPos(agId, il);
                return true;
        }
        	
            /*Location independant = getAgPos(0);
            independant.x++;
            if (independant.x == getWidth()) {
                independant.x = 0;
                independant.y++;
            }
            // finished searching the whole grid
            if (independant.y == getHeight()) {
                independant.y = 0;
            }
            setAgPos(0, independant);
            //setAgPos(1, getAgPos(1)); // just to draw it in the view*/

        boolean moveTowards(int x, int y, int agId) throws Exception {
            Location il = getAgPos(agId);
            
            /*if (il.x < x && isFree(il.x+1, il.y)) {
            	il.x++;
            } else if (il.x > x && isFree(il.x-1, il.y)) {
                il.x--;
            }
            if (il.y < y && isFree(il.x, il.y+1)) {
                il.y++;
            }else if (il.y > y && isFree(il.x, il.y-1)) {
                il.y--;
            }*/
            
            if (il.x < x) {
            	il.x++;
            } else if (il.x > x ) {
                il.x--;
            }
            if (il.y < y ) {
                il.y++;
            }else if (il.y > y) {
                il.y--;
            }
            
            setAgPos(agId, il);
            return true;
        }

       /* void pickGarb() {
            // independant location has garbage
            if (model.hasObject(GARB, getAgPos(0))) {
                // sometimes the "picking" action doesn't work
                // but never more than MErr times
                if (random.nextBoolean() || nerr == MErr) {
                    remove(GARB, getAgPos(0));
                    nerr = 0;
                    independantHasGarb = true;
                } else {
                    nerr++;
                }
            }
        }
        void dropGarb() {
            if (independantHasGarb) {
                independantHasGarb = false;
                add(GARB, getAgPos(0));
            }
        }
        void burnGarb() {
            // r2 location has garbage
            if (model.hasObject(GARB, getAgPos(1))) {
                remove(GARB, getAgPos(1));
            }
        }*/
    }
    
    
    class EvView extends GridWorldView {

        public EvView(EvModel model) {
            super(model, "Ev World", 600);
            defaultFont = new Font("Arial", Font.BOLD, 18); // change default font
            setVisible(true);
            repaint();
        }    
        
        @Override
        public void initComponents(int width) {
            super.initComponents(width);
            
            getCanvas().addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    int col = e.getX() / cellSizeW;
                    int lin = e.getY() / cellSizeH;
                    if (col >= 0 && lin >= 0 && col < getModel().getWidth() && lin < getModel().getHeight()) {
                        EvModel wm = (EvModel)model;
                        wm.add(EvModel.FIRE, col, lin);
                        update(col, lin);
                        
                    }
                }
                public void mouseExited(MouseEvent e) {}
                public void mouseEntered(MouseEvent e) {}
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
            });

        }
        
        /** draw application objects */
        @Override
        public void draw(Graphics g, int x, int y, int object) {
            switch (object) {
            case EvModel.DOOR:
                drawDoor(g, x, y);
                break;
            case EvModel.FIRE:
            	drawFire(g, x, y);
            	break;
            }
        }

        @Override
        public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        	String label = "R"+(id+1);
            c = Color.blue;
            /*if (id == 0) {
            	// color amarillo de la aspiradora
                c = Color.yellow;
                // si lleva basura entonces es naranja
                if (((EvModel)model).independantHasGarb) {
                    label += " - G";
                    c = Color.orange;
                }
            }*/
            super.drawAgent(g, x, y, c, -1);
           
                g.setColor(Color.black);
                
            super.drawString(g, x, y, defaultFont, label);
            repaint();
        }

        public void drawDoor(Graphics g, int x, int y) {
            super.drawObstacle(g, x, y);
            g.setColor(Color.white);
            drawString(g, x, y, defaultFont, "DOOR");
        }
        
        public void drawFire(Graphics g, int x, int y) {
        	/*super.drawObstacle(g, x, y);
            g.setColor(Color.red);
            drawString(g, x, y, defaultFont, "F");*/
        	g.setColor(Color.red);
            g.drawRect(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
            int[] vx = new int[4];
            int[] vy = new int[4];
            vx[0] = x * cellSizeW + (cellSizeW / 2);
            vy[0] = y * cellSizeH;
            vx[1] = (x + 1) * cellSizeW;
            vy[1] = y * cellSizeH + (cellSizeH / 2);
            vx[2] = x * cellSizeW + (cellSizeW / 2);
            vy[2] = (y + 1) * cellSizeH;
            vx[3] = x * cellSizeW;
            vy[3] = y * cellSizeH + (cellSizeH / 2);
            g.fillPolygon(vx, vy, 4);
            EvEnv.fire = true;
            
        }

    }
}