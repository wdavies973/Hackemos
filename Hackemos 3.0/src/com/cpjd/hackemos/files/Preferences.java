package com.cpjd.hackemos.files;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;

public class Preferences {
	private File prefs;

	public Preferences() {
		try {
			createFile();
		} catch(Exception e) {
			System.err.println("Failed to create preferences file.");
		}
	}
	
	private void createFile() throws Exception {
		File currentDir = new File(Preferences.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		prefs = new File(currentDir.getParentFile().getAbsoluteFile() + "/hackemos-prefs.txt");
		if(!prefs.exists()) {
			prefs.createNewFile();
			
			String[] defaults = {
					"9", // number of items
					"100", // correct limit
					"0", // amount to get wrong
					"75", // delay, in ms
					"456,278", // default mouse position for start button
					"686,615", // default mouse position for the text box
					"indicative",
					"present"
			};
			
			write(defaults);
		}
	}
	
	public void destroy() throws Exception {
		System.out.println("Hasta la vista baby");
		prefs.deleteOnExit();
		System.exit(0);
	}
	
	/**
	 * Preferance names: climit, wrong, delay
	 * @param prefname
	 * @param value
	 */
	public void savePreference(String prefname, String value) {
		prefname = prefname.toLowerCase();
		
		switch(prefname) {
		case "climit":
			write(value, 1);
			System.out.println("climit value updated.");
			break;
		case "wrong":
			write(value, 2);
			System.out.println("wrong amount value updated.");
			break;
		case "delay":
			write(value, 3);
			System.out.println("delay value updated.");
			break;
		case "category":
			write(value, 6);
			System.out.println("category value updated.");
			break;
		case "type":
			write(value, 7);
			System.out.println("type value updated.");
			break;
		}
	}
	/**
	 * Preferance names: start, copy, answer, cmotion
	 * @param calname
	 * @param value
	 */
	public void saveCalibration(String calname, String value) {
		switch (calname) {
		case "start":
			write(value, 4);
			break;
		case "answer":
			write(value, 5);
			break;
		}
	}
	public int getCorrectLimit() {
		return Integer.parseInt(read(1));
	}
	
	public int getWrong() {
		return Integer.parseInt(read(2));
	}
	
	public int getDelay() {
		return Integer.parseInt(read(3));
	}
	
	public Point getStart() {
		return readPoints(4);
	}
	
	public String getCategory() {
		return read(6);
	}
	
	public String getType() {
		return read(7);
	}
	
	public Point getAnswer() {
		return readPoints(5);
	}
	public Point getMotion() {
		return readPoints(7);
	}
	
	private Point readPoints(int line) {
		String l = read(line);
		try {
			return new Point((int)Double.parseDouble(l.split(",")[0]), (int)Double.parseDouble(l.split(",")[1]));
		} catch(Exception e) {
			e.printStackTrace();
			return new Point(0,0);
		}
	}
	
	private void write(String data, int line) {
		String[] prev = new String[Integer.parseInt(read(0))];
		for(int i = 0; i < prev.length; i++) {
			prev[i] = read(i);
		}
		prev[line] = data;
		write(prev);
	}
	
	private void write(String[] data) {
		try {
			FileOutputStream os = new FileOutputStream(prefs);
			PrintWriter out = new PrintWriter(os);

			for (int i = 0; i < data.length; i++) {
				out.println(data[i]);
			}

			out.close();
		} catch (Exception e) {
			System.err.println("Failed to write to the preferences file.");
		}
	}
	
	private String read(int line) {

		try {
			FileReader fr = new FileReader(prefs);
			BufferedReader br = new BufferedReader(fr);

			for (int i = 0; i < line; i++) {
				br.readLine();
			}
			String temp = br.readLine();

			br.close();
			return temp;

		} catch (Exception e) {
			System.err.println("Failed to read preferences file");
			return "null";
		}
	}
	
}
