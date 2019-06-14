
package com.krupizde.command;

import java.sql.SQLException;
import com.krupizde.persistence.DaoUserProfiles;
import com.krupizde.persistence.FileIOWorker;

/**
 * Class represents command, that exports all valid profiles to given file 'Valds.txt'
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class ExportCommand extends Command {
	public ExportCommand() {
		super();
	}

	public void execute() {
		System.out.println("Trying to export profiles");
		try {
			FileIOWorker.getInstance().exportNames("Valids.txt", DaoUserProfiles.getDao().getValidProfiles());
		} catch (SQLException e) {
			System.err.println("Could not finish export (" + e.getMessage() + ")");
		}
		System.out.println("Exported");
	}

	public String executePhrase() {
		return "export-valids";
	}

	public String description() {
		return "Exports names of all valid profiles into file 'Valids.txt'";
	}
}
