
package com.krupizde.command;

import com.krupizde.starts.InteractiveStart;

/**
 * Class represents command, that ends interactive mode of application
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class ExitCommand extends Command {
	public ExitCommand() {
		super();
	}

	public void execute() {
		System.out.println("Exiting app");
		InteractiveStart.exit();
	}

	public String executePhrase() {
		return "exit";
	}

	public String description() {
		return "Shuts down the application.";
	}
}
