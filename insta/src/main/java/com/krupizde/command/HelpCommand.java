
package com.krupizde.command;

/**
 * Class represents command, that prints all avaible command and their
 * descriptions to console
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class HelpCommand extends Command {
	public HelpCommand() {
		super();
	}

	public void execute() {
		for (final String s : CommandControl.getCommands()) {
			System.out.println(s);
		}
	}

	public String executePhrase() {
		return "help";
	}

	public String description() {
		return "Prints all avaible commands and their descriptions.";
	}
}
