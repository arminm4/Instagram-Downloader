/**
 * 
 */
package com.krupizde.command;

/**
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class SetPriorityCommand extends Command{

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String executePhrase() {
		// TODO Auto-generated method stub
		return "priority";
	}

	@Override
	public String description() {
		return "Allows user to set priorities as number 1 - 10000 to multiple profiles, which will prioritize their mediums in downloading iteration.";
	}
	
}
