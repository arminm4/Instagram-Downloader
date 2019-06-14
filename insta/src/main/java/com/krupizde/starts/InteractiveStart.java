package com.krupizde.starts;

import java.util.Scanner;

import com.krupizde.command.CommandControl;
/**
 * Class represents Starting method, where application takes input from user and executes given commands 
 * over database and saved data
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class InteractiveStart extends Start {

	private static boolean interact = true;

	public static void exit() {
		interact = false;
	}

	@Override
	public void start() {
		System.out.println("Started in interactive mode\nType help to see avaible commands");
		@SuppressWarnings("resource")
		final Scanner scan = new Scanner(System.in);
		final CommandControl com = new CommandControl();
		while (interact) {
			final String command = scan.nextLine();
			com.execute(command);
		}
	}

	@Override
	public String startParam() {
		return "-i";
	}

	@Override
	public String description() {
		return "Will start application in interactive mode. You are the able to perform actions over appications database, delete non-valid profiles etc...";
	}

	@Override
	public String name() {
		return "Interactive start";
	}

}
