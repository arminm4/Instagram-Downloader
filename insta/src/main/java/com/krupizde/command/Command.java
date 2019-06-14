package com.krupizde.command;

/**
 * Parent class for command pattern used in interactive mode.
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public abstract class Command {
	/**
	 * Executing method for given command
	 */
	public abstract void execute();

	/**
	 * Method compares given command execute phrase with user input and decides if
	 * they equals
	 * 
	 * @param paramString User command input
	 * @return True if user input equals command execution phrase, false otherwise
	 */
	public boolean canExecute(String paramString) {
		return executePhrase().equals(paramString);
	}

	/**
	 * 
	 * @return String execution phrase of given command
	 */
	public abstract String executePhrase();

	/**
	 * 
	 * @return String description of given command
	 */
	public abstract String description();
}