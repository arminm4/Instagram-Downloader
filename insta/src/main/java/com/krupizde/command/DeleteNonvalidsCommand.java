package com.krupizde.command;

import com.krupizde.persistence.DaoUserProfiles;
import com.krupizde.persistence.FileIOWorker;
import java.sql.SQLException;

/**
 * Class representing command for exporting and deleting non-valid profiles from
 * database
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class DeleteNonvalidsCommand extends Command {
	public DeleteNonvalidsCommand() {
	}

	public void execute() {
		DaoUserProfiles uDao = DaoUserProfiles.getDao();
		System.out.println("Exporting non-valid users");
		try {
			java.util.ArrayList<com.krupizde.instagram.entities.Profile> nonValids = uDao.getNonValids();
			FileIOWorker.getInstance().exportNames("Export.txt", nonValids);
			System.out.println("Saved");
		} catch (SQLException localSQLException) {
		}
		System.out.println("Deleting non-valid users");
		uDao.deleteNonValids();
		System.out.println("Deleted");
	}

	public String executePhrase() {
		return "delete-expired";
	}

	public String description() {
		return "Deletes all non-valid profiles from database and exports them to file 'Export.txt'.";
	}
}