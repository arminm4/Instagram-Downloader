
package com.krupizde.command;

import java.util.ArrayList;
import com.krupizde.instagram.entities.Profile;
import java.sql.SQLException;
import com.krupizde.persistence.DaoUserProfiles;

/**
 * Class represents command, that prints all profiles from database to console
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class ListCommand extends Command {
	public ListCommand() {
		super();
	}

	public void execute() {
		System.out.println("Listing all profiles");
		ArrayList<Profile> profiles = null;
		try {
			profiles = (ArrayList<Profile>) DaoUserProfiles.getDao().getAllProfiles();
					
		} catch (SQLException e) {
			System.err.println("Cannot list profiles");
		}
		for (final Profile p : profiles) {
			System.out.println(String.valueOf(p.getName()) + "\t" + (p.isValid() ? "Valid" : "Non-valid"));
		}
	}

	public String executePhrase() {
		return "list";
	}

	public String description() {
		return "List all profiles stored in database.";
	}
}
