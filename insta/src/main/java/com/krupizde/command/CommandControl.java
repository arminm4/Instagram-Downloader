package com.krupizde.command;

import java.util.ArrayList;

/**
 * An execution class for command pattern
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class CommandControl {
	private static ArrayList<Command> commands;

	/**
	 * Constructror adding all commands to arraylist
	 */
	public CommandControl() {
		commands = new ArrayList<Command>();
		commands.add(new LoadNamesCommand());
		commands.add(new GetNonvalidsCommand());
		commands.add(new DeleteNonvalidsCommand());
		commands.add(new ResetDatabase());
		commands.add(new ExitCommand());
		commands.add(new HelpCommand());
		commands.add(new ListCommand());
		commands.add(new ExportCommand());
		commands.add(new CheckCommand());
		commands.add(new GetPrivatesCommand());
		commands.add(new SetPriorityCommand());
	}

	/**
	 * @return List strings composed of command execution phrases and their
	 *         descriptions
	 */
	public static ArrayList<String> getCommands() {
		ArrayList<String> tmp = new ArrayList<String>();
		for (Command c : commands) {
			tmp.add(">>> " + c.executePhrase() + "\t-- " + c.description());
		}
		return tmp;
	}

	/**
	 * Method to execute commands based o user input
	 * 
	 * @param comm User input
	 */
	public void execute(String comm) {
		for (Command c : commands) {
			if (c.canExecute(comm)) {
				c.execute();
				return;
			}
		}
		System.out.println("Unknown command, use 'help' to list commands");
	}
}