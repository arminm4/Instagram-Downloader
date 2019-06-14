// Decompiled using: fernflower
// Took: 41ms

package com.krupizde.command;

import com.krupizde.persistence.DaoUserProfiles;
import com.krupizde.main.Configuration;

/**
 * Class represents command, that loads all names from set file to database
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class LoadNamesCommand extends Command {
	public LoadNamesCommand() {
		super();
	}

	public void execute() {
		System.out.println("Loading names from " + Configuration.getConfig().getNamesPath() + " and setting "
				+ Configuration.getConfig().getSavePath() + " as savePath");
		DaoUserProfiles.getDao().addUsersAndProfiles(Configuration.getConfig().getNamesPath(),
				Configuration.getConfig().getSavePath());
		System.out.println("Names loaded");
	}

	public String executePhrase() {
		return "load-names";
	}

	public String description() {
		return "Loads names from set file and saves them to database.";
	}
}
