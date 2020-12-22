package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JComponent;

public class numberButton extends JComponent implements MouseListener{

	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

	int width = 30;
	int height = width;
	
	int x;
	int y;
	
	boolean selected;
	int number;
	
	public numberButton(int number) {
		
		setSize(width, height);
		enableInputMethods(true);
		addMouseListener(this);
		setFocusable(true);
		
		this.number = number;
		
		repaint();
		
	}
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		/* Changes border of cell to green if selected, black if not selected */
		if(this.selected) {
			
			g.setColor(new Color(0, 189, 22));
			
		} else if(!(this.selected)) {
			
			g.setColor(Color.black);
			
		}
		
		g.fillRect(x, y, width, height);
		
		g.setColor(Color.white);
		g.fillRect(x + 1, y + 1, width - 2, height - 2);
		
		g.setColor(Color.black);
		g.drawString(String.valueOf(this.getNumber()), x + 11, y + 20);
		
	}
	
	public void addActionListener(ActionListener listener) {
		
		listeners.add(listener);
		
	}
	
	public void notifyListeners(MouseEvent e) {
		
		ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, new String(), e.getWhen(), e.getModifiers());
		synchronized(listeners) {
			
			for(int i = 0; i < listeners.size(); i++) {
				
				ActionListener tmp = listeners.get(i);
				tmp.actionPerformed(evt);
				
			}
			
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
				
		notifyListeners(e);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	/* Auto-generated getters and setters */
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}
	
}
