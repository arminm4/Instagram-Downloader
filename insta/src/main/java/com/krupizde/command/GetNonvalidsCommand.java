
package com.krupizde.command;

import java.sql.SQLException;
import java.util.ArrayList;
import com.krupizde.persistence.FileIOWorker;
import com.krupizde.instagram.entities.Profile;
import com.krupizde.persistence.DaoUserProfiles;

/**
 * Class representing command, that prints non-valid profiles to console and
 * exports them to file 'Exports.txt'
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class GetNonvalidsCommand extends Command {
	public GetNonvalidsCommand() {
		super();
	}

	public void execute() {
		System.out.println("Exporting non-valids");
		try {
			final ArrayList<Profile> profiles = (ArrayList<Profile>) DaoUserProfiles.getDao().getNonValids();
			for (final Profile p : profiles) {
				System.out.println(p.getName());
			}
			FileIOWorker.getInstance().exportNames("Export.txt", (ArrayList<Profile>) profiles);
		} catch (SQLException e) {
			System.err.println("Could not get non-valid profiles");
		}
		System.out.println("Exported");
	}

	public String executePhrase() {
		return "get-expired";
	}

	public String description() {
		return "Exports all non-valid profiles from database to file 'Export.txt'";
	}
}
