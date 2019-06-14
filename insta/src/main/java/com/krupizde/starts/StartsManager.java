package com.krupizde.starts;

import java.util.ArrayList;
import java.util.List;
/**
 * Manager class for executing starting methods of this application. Based on executing of commands from command pattern.
 * There exists only one instance in whole application
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class StartsManager {
	/**
	 * Private constructor which adds all starting methods into list
	 */
	private StartsManager() {
		starts = new ArrayList<Start>();
		starts.add(new InteractiveStart());
		starts.add(new CrawlingOnlyStart());
		starts.add(new DownloadFirstStart());
		starts.add(new DownloadOnlyStart());
		starts.add(new DiscreteStart());
		starts.add(new StartHelp());
	}

	private static StartsManager manager;

	/**
	 * Method to get the only instance of StartsManager in whole application.
	 * Singleton
	 * @return Always only one StartsManager instance
	 */
	public static StartsManager getManager() {
		if (manager == null) {
			manager = new StartsManager();
		}
		return manager;
	}

	private static List<Start> starts;

	/**
	 * Method that executes starting method from given phrase
	 * @param phrase
	 */
	public void start(String phrase) {
		for (Start s : starts) {
			if (s.isSelected(phrase)) {
				s.start();
				break;
			}
		}
	}

	/**
	 * @return Returns list of string composed of StartingMethod name, execution phrase and description
	 */
	public static List<String> getPhrasesAndDesc() {
		List<String> phrases = new ArrayList<String>();
		for (Start s : starts) {
			phrases.add(">>> " + s.name() + " '" + s.startParam() + "'\t--" + s.description());
		}
		return phrases;
	}
}
