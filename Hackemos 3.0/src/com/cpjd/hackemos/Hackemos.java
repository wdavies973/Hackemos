package com.cpjd.hackemos;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import com.cpjd.hackemos.files.Preferences;
import com.cpjd.main.Conjugator;

public class Hackemos implements Runnable {
	private Preferences prefs;
	
	private Thread thread;
	private boolean running;
	private int delay;
	private Robot r;
	
	private Point start, answer;
	
	private int correctLimit;
	private int correct;
	
	private int wrong;
	
	private boolean copyError;
	
	private Conjugator conj;
	
	private String category, type;
	
	public Hackemos() {
		copyError = false;
		correct = 0;
		
		conj = new Conjugator();
		
		prefs = new Preferences();
		
		start = prefs.getStart();
		answer = prefs.getAnswer();
		delay = prefs.getDelay();
		correctLimit = prefs.getCorrectLimit();
		wrong = prefs.getWrong();
		category = prefs.getCategory();
		type = prefs.getType();
		
		System.out.println("Starting Hackemos with these values:");
		System.out.println("Correct limit: "+correctLimit);
		System.out.println("Number to get wrong: "+wrong);
		System.out.println("Delay (ms): "+delay+ " Actual delay (ms): "+(delay * 2 + 100));
		System.out.println("Correct p/sec: "+(correctLimit / ((delay * 2 + 100)) / 1000));
		System.out.println("Start location: "+start.x+","+start.y);
		System.out.println("Answer location: "+answer.x+","+answer.y);
		System.out.println("Category: "+category);
		System.out.println("Type: "+type);
		
		try {
			r = new Robot();
			clickStart();
			messUp();
		} catch(Exception e) {e.printStackTrace();}
		
		thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		running = true;
		
		while(running) {
			try {
				copy();
				Thread.sleep(delay);
				type();
				correct++;
				if(correct >= correctLimit) stop();
			} catch(Exception e) {
				System.out.println("Problme");
				e.printStackTrace();
			}
		}
	}
	
	
	/*
	 * Macro commands
	 */
	
	private void clickStart() {
		r.mouseMove(start.x, start.y);
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		r.mouseWheel(-100);
		r.delay(500);
	}
	
	private void copy() throws AWTException {
		r.mouseMove(2, start.y);
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        r.keyPress(KeyEvent.VK_CONTROL);
        r.keyPress(KeyEvent.VK_A);
        r.keyRelease(KeyEvent.VK_A);
        r.keyPress(KeyEvent.VK_C);
        r.keyRelease(KeyEvent.VK_CONTROL);
        r.keyRelease(KeyEvent.VK_C);
	}
	
    private void type() {
    	try {
    		r.mouseWheel(-40);
    		r.mouseMove(answer.x, answer.y);
        	r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        	r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        	String text = getClipBoard();
        	
        	String[] tokens = text.split("\\s+");
        	
        	String verb = "";
        	String pronoun = "";
        	
        	for(int i = 0; i < tokens.length; i++) {
        		if(tokens[i].trim().equalsIgnoreCase("tutorial.")) {
        			verb = tokens[i + 2].trim();
        			
        			pronoun = tokens[i + 1].trim();
        			break;
        		}
        	}
        	type(conj.getVerbConjugation(verb, category, type, pronoun));
        	
        	r.keyPress(KeyEvent.VK_ENTER);
        	r.keyRelease(KeyEvent.VK_ENTER);
    	} catch(Exception e) {
    		e.printStackTrace();
    		if(!copyError) {
    			System.err.println("Didn't find anything to copy, try recalibrating.");
    			copyError = true;
    		}
    	}
	}
    
    public String getPrestarConjugation(String pronoun) {
    	if(pronoun.equals("yo") || pronoun.equals("usted")) return "preste";
    	else if(pronoun.equals("tú")) return "prestes";
    	else if(pronoun.equals("él") || pronoun.equals("ella") || pronoun.equals("ustedes")) return "preste";
    	else if(pronoun.equals("nosotros")) return "prestemos";
    	else {
    		return "presten";
    	}
    }
    
	public void type(CharSequence cs){
		System.out.println(cs);
		String s = cs.toString().toLowerCase();
		cs = s;
        for(int i=0;i<cs.length();i++){
            type(cs.charAt(i));
        }
    }

	public void type(char c) {
		r.keyPress(KeyEvent.VK_ALT);
		r.keyPress(KeyEvent.VK_NUMPAD0);
		r.keyRelease(KeyEvent.VK_NUMPAD0);
		String altCode = Integer.toString(c);
		for (int i = 0; i < altCode.length(); i++) {
			c = (char) (altCode.charAt(i) + '0');
			r.delay(3);// may be needed for certain applications
			r.keyPress(c);
			r.keyRelease(c);
		}
		r.keyRelease(KeyEvent.VK_ALT);
	}
    
	public void setCategory(String category) {
		this.category = category;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
    private String getClipBoard(){
	    try {
	        return (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return "";
	}
	private void messUp() {
    	r.mouseMove(answer.x, answer.y);
    	r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        r.keyPress(KeyEvent.VK_T);
    	r.keyRelease(KeyEvent.VK_T);
    	for (int i = 0; i < wrong; i++){
    		r.keyPress(KeyEvent.VK_ENTER);
    		r.keyRelease(KeyEvent.VK_ENTER);
    		r.delay(100);
    	}
    	r.keyPress(KeyEvent.VK_BACK_SPACE);
		r.keyRelease(KeyEvent.VK_BACK_SPACE);
	}
	public void stop() {
		try {
			running = false;
			thread.join();
		} catch(Exception e) {}
	}
	
}
