package com.krupizde.starts;
/**
 * Class represents Starting Method without any input phrase. It prints all Starting methods to user with their descriptions.
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class StartHelp extends Start {

	@Override
	public void start() {
		System.out.println("");
		for (String s : StartsManager.getPhrasesAndDesc()) {
			System.out.println(s);
		}
	}

	@Override
	public boolean isSelected(String phrase) {
		return true;
	}

	@Override
	public String startParam() {
		return "";
	}

	@Override
	public String description() {
		return "Prints all application start possibilities";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Help";
	}

}
