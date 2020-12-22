package main;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class boardComp extends JComponent{

	int width;
	int height;
	
	int big = 300;
	int small = 2;
	
	int x;
	int y;
	
	public boardComp(String layout) {
		
		if(layout.contentEquals("horizontal")) {
			
			width = big;
			height = small;
			this.setSize(width, height);
			
		} else if (layout.contentEquals("vertical")) {
			
			width = small;
			height = big;
			this.setSize(width, height);
			
		}
		
		repaint();
		
	}
	
	public void paintComponent(Graphics g) {
		
		g.setColor(Color.black);
		g.fillRect(x, y, width, height);
		
	}
	
}
