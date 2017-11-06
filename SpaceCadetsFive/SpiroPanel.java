import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

public class SpiroPanel extends JPanel{

	int height;
	int width;
	int prevY;
	int prevX;
	int x;
	int y;
	
	public SpiroPanel(int width, int height) {
		this.height = height;
		this.width = width;
	}
	
	public void draw(Hypercycloid hc, Graphics g) {
		Random randNum = new Random();
		g.setColor(new Color(randNum.nextInt(255), randNum.nextInt(255), randNum.nextInt(255)));
		double time = 0;
		prevX = (int) ((hc.radius1 + hc.radius2) * Math.cos(time) - (hc.radius1 + hc.offset) * Math.cos(((hc.radius2 + hc.radius1) / hc.radius1) * time)) + hc.xPos;
		prevY = (int) ((hc.radius1 + hc.radius2) * Math.sin(time) - (hc.radius1 + hc.offset) * Math.sin(((hc.radius2 + hc.radius1) / hc.radius1) * time)) + hc.yPos;
		
		while(time < 1000) {
			
			time += 0.1;
			
			x = (int) ((hc.radius1 + hc.radius2) * Math.cos(time) - (hc.radius1 + hc.offset) * Math.cos(((hc.radius2 + hc.radius1) / hc.radius1) * time)) + hc.xPos;
			y = (int) ((hc.radius1 + hc.radius2) * Math.sin(time) - (hc.radius1 + hc.offset) * Math.sin(((hc.radius2 + hc.radius1) / hc.radius1) * time)) + hc.yPos;
		
			paintComponent(g);
			
			prevX = x;
			prevY = y;
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawLine(prevX, prevY, x, y);
	}
	
	
}
