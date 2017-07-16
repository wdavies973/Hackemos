package com.cpjd.hackemos;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.cpjd.hackemos.files.Preferences;

public class Calibration implements KeyListener {
	
	private JFrame frame;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int stage;
	private boolean noTutorials = false;
	private Preferences prefs;
	
	public Calibration(boolean noTutorials) {
		this.noTutorials = noTutorials;
		prefs = new Preferences();
		stage = 0;
		
		frame = new JFrame("Calibrate");
		
		frame.setSize(200, 200);
		frame.setLocation((int) screenSize.getWidth() - 200,(int) screenSize.getHeight() - 200);
		frame.addKeyListener(this);
		frame.requestFocus();
		frame.setVisible(true);
		
		System.out.println("Calibration started. First, open conjugemos and make it full screen. Then click on the small box window opened by Hackemos, click in it. Make sure the hackemos window has focus.");
		
		if(!noTutorials) {
			System.out.println("1) Hover your mouse over the start button, and press 'x'");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() != KeyEvent.VK_X) return;
		Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
		
		switch(stage) {
		case 0:
			prefs.saveCalibration("start", mouseLoc.getX() + "," + mouseLoc.getY());
			System.out.println("Start button calibrated.");
			if(!noTutorials) {
				System.out.println("2) Hover your mouse over the answer box, and press 'x'");
			}
			stage++;
			break;
		case 1:
			prefs.saveCalibration("answer", mouseLoc.getX() + "," + mouseLoc.getY());
			System.out.println("Calibration finished");
			System.out.println("Type a command. Type 'help' for a list of commands");
			frame.dispose();
			break;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {}
	
}
