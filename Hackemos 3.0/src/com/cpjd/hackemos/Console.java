package com.cpjd.hackemos;

import java.util.Scanner;

import com.cpjd.hackemos.files.Preferences;

public class Console {
	
	private Scanner scanner;
	private Preferences prefs;
	private Hackemos hackemos;
	
	public Console() {
		 prefs = new Preferences();
		
		openConsole();
	}
	
	private void openConsole() {
		System.out.println("Welcome to Hackemos 3.0. Type 'help' to list commands.");
		
		scanner = new Scanner(System.in);
		
		do {
			try {
				processCmd(scanner.nextLine());
			} catch(Exception e) {
				System.out.println("Command syntax incorrect. Please try again.");
				e.printStackTrace();
			}
		} while(true);
		
	}
	
	private void processCmd(String command) throws Exception {
		String[] tokens = command.toLowerCase().split("\\s+");
		
		switch (tokens[0]) {
		case "help":
			if(tokens.length > 1) {
				if(tokens[1].equals("cpref")) System.out.println("Change preference usage: cpref <prefence-name> <new-value>");
				else if(tokens[1].equals("cal")) System.out.println("Calibration usage: cal <add -t here to disable tutorials>");
				else System.out.println("No extra parameters.");
			} else {
				System.out.println("Type HELP <command-name> to list command usage.");
				System.out.println("START		Starts the program.");
				System.out.println("CAL		Launches calibration mode.");
				System.out.println("HELP		Lists all commands.");
				System.out.println("CPREF		Changes a preference.");
				System.out.println("LPREF		Lists all the preferences.");	
				System.out.println("STOP		Stops the application.");
				System.out.println("PANIC		Instantly deletes hackemos-prefs.txt, and the database.txt");
			}
			break;
		case "stop":
			System.out.println("Stopping Hackemos....");
			if(hackemos != null) hackemos.stop();
			scanner.close();
			System.exit(0);
			break;
		case "lpref":
			System.out.println("Available Hackemos preferences. Change these with the cpref command.");
			System.out.println("Preference name        Possible values        Description");
			System.out.println("climit                 0-500                  Limits the amount of correct answers");
			System.out.println("wrong                  0-500                  The amount of answers to get wrong");
			System.out.println("delay                  0-2000                 The time, in milliseconds, to delay between answers");
			System.out.println("cmotion                x,y                    The x and y distances to travel to copy the problem text");
			System.out.println("category               Indicative, Subjunctive, Imperative Affirmative, Imperative Neggative");
			System.out.println("type                   Indicative: Present, Future, Imperfect, Preterite, Conditional, Present Perfect, Future Perfect, Past Perfect, Preterite (Archaic), Conditional Perfect. Subjunctive: Present, Imperfect, Future, Present Perfect, Future Perfect, Past Perfect Imperative (a/n): Present");
			break;
		case "cpref":
			if(tokens[1].equals("climit")) {
				if(Integer.parseInt(tokens[2]) > 500) {
					System.out.println("Cannot set value higher than 500.");
					break;
				}
				prefs.savePreference("climit", tokens[2]);
			}
			else if(tokens[1].equals("wrong")) {
				if(Integer.parseInt(tokens[2]) > 500) {
					System.out.println("Cannot set value higher than 500.");
					break;
				}
				prefs.savePreference("wrong", tokens[2]);
			}
			else if(tokens[1].equals("delay")) {
				if(Integer.parseInt(tokens[2]) > 5000) {
					System.out.println("Cannot set value higher than 1000.");
					break;
				}
				prefs.savePreference("delay", tokens[2]);
			} else if(tokens[1].equals("category")) {
				prefs.savePreference("category", tokens[2]);
				if(hackemos != null) hackemos.setCategory(tokens[2]);
			} else if(tokens[1].equals("type")) {
				prefs.savePreference("type", tokens[2]);
				if(hackemos != null) hackemos.setType(tokens[2]);
			}
			else System.out.println("No preference found with that name.");
			break;
		case "panic":
			try {
				prefs.destroy();
			} catch(Exception e) {
				System.out.println("Panic mode failed. You're screwed.");
			}
			break;
		case "start":
			hackemos = new Hackemos();
			break;
		case "cal":
			if(tokens.length > 1 && tokens[1].equals("-t")) new Calibration(true);
			else new Calibration(false);
			break;
		default:
			System.err.println("Command unrecognized. Type 'help' for a list of commands.");
			break;
		}
		
	}
	
	public static void main(String[] args) {
		new Console();
	}
	
}
