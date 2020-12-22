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

public class Cell extends JComponent implements MouseListener{

	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
	
	boolean mouseEntered;
	boolean mousePressed;
	
	int width = 33;
	int height = width;
	
	int x;
	int y;
	
	int number;
	int input;
	int inputtingNum;
	boolean solved;
	boolean incorrect;
	boolean inputting;
	boolean correct;
	
	public Cell(int number) {
		
		setSize(width, height);
		
		enableInputMethods(true);
		addMouseListener(this);
		setFocusable(true);
		
		this.number = number;
		solved = true;
		input = 0;
		inputting = false;
		
		repaint();
		
	}
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		/* Changes color based on mouse actions (hovering or pressed) */
		if(mouseEntered) {
			
			g.setColor(new Color(209, 209, 209));
	
		} else if(mousePressed) {
			
			g.setColor(new Color(168, 168, 168));
			
		} else {
			
			g.setColor(Color.white);
			
		}
		
		/* Changes cell to green if correct, red if incorrect. */
		if(correct) {
			
			g.setColor(new Color(59, 209, 89));
			
		} else if(incorrect) {
			
			g.setColor(new Color(245, 127, 127));
			
		} 
		
		/* Creates cell */
		g.fillRect(x, y, width, height);
		g.setColor(Color.black);
		g.drawRect(x, y, width, height);
		
		g.setColor(Color.black);
		
		/* Display a number if cell is solved or user has inputed a number. */
		if(solved) {
			
			g.drawString(String.valueOf(this.getNumber()), x + 13, y + 22);
			
		} else {
			
			if(inputting) {
				
				g.drawString(String.valueOf(this.inputtingNum), x + 13, y + 22);
				
			}
			
		}
		
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
		
		if(this.isEnabled()) {
			
			mousePressed = true;
			mouseEntered = false;
			notifyListeners(e);
			repaint();	
			
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		mouseEntered(e);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		mouseEntered = true;
		mousePressed = false;
		repaint();
				
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		mouseEntered = false;
		mousePressed = false;
		repaint();
		
	}

	/* Auto-generated getters and setters */
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
		repaint();
	}

	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
		this.setEnabled(!solved);
		repaint();
	}

	public int getInput() {
		return input;
	}

	public void setInput(int input) {
		this.input = input;
	}

	public boolean isInputting() {
		return inputting;
	}

	public void setInputting(boolean inputting) {
		this.inputting = inputting;
	}

	public int getInputtingNum() {
		return inputtingNum;
	}

	public void setInputtingNum(int inputtingNum) {
		this.inputtingNum = inputtingNum;
		repaint();
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
		repaint();
	}

	public boolean isIncorrect() {
		return incorrect;
	}

	public void setIncorrect(boolean incorrect) {
		this.incorrect = incorrect;
		repaint();
	}
	
}
