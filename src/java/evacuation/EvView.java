package evacuation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import jason.environment.grid.GridWorldView;

public class EvView extends GridWorldView {
	EvModel mod;

	public EvView(EvModel model) {
		super(model, "Ev World", 600);
		mod = model;
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
				if (col >= 0 && lin >= 0 && col < getModel().getWidth()
						&& lin < getModel().getHeight()) {
					EvModel evm = (EvModel) model;
					evm.add(EvModel.EMERGENCY, col, lin);
					update(col, lin);
				}
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
		});

	}

	/** draw application objects */
	@Override
	public void draw(Graphics g, int x, int y, int object) {
		switch (object) {
		case EvModel.DOOR:
			drawDoor(g, x, y);
			break;
		case EvModel.EMERGENCY:
			drawEmergencyBlock(g, x, y);
			break;
		}
	}

	@Override
	public void drawAgent(Graphics g, int x, int y, Color c, int id) {
	
			String label;
			
			if (id < 10) {
				super.drawAgent(g, x, y, Color.BLUE, -1);
				label = "" + (id + 1);
			} else if (id > 9 && id < 13) {
				super.drawAgent(g, x, y, Color.GREEN, -1);
				label = "" + (id - 9);
			} else {
				super.drawAgent(g, x, y, Color.YELLOW, -1);
				label = "" + (id - 12);
			}

			g.setColor(Color.black);

			super.drawString(g, x, y, defaultFont, label);
			
	}

	public void drawDoor(Graphics g, int x, int y) {
		super.drawObstacle(g, x, y);
		g.setColor(Color.white);
		drawString(g, x, y, defaultFont, "D");
	}

	public void drawEmergencyBlock(Graphics g, int x, int y) {
		super.drawObstacle(g, x, y);
		g.setColor(Color.red);
		g.drawRect(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4,
				cellSizeH - 4);
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
		EvModel.setEmergency();
	}

}