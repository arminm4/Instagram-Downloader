package com.krupizde.starts;
/**
 * Parent method for all possible application starting methods. Based on command pattern.
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public abstract class Start {

	/**
	 * Executing method for given starting method
	 */
	public abstract void start();
	/**
	 * Method returns executing phrase for given starting method
	 * @return
	 */
	public abstract String startParam();
	/**
	 * Method compares starting phrase from parameter and starting phrase of given starting method
	 * @param phrase Given starting phrase from user
	 * @return True if phrase equals with starting phrase of this method, false otherwise
	 */
	public boolean isSelected(String phrase) {
		return startParam().equals(phrase);
	}
	/**
	 * @return Return description of given starting method
	 */
	public abstract String description();
	/**
	 * 
	 * @return Name of given starting method
	 */
	public abstract String name();
}
