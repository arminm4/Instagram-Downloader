package com.krupizde.command;

import java.sql.SQLException;

import com.krupizde.persistence.DaoUserProfiles;
import com.krupizde.persistence.FileIOWorker;
/**
 * Class represents command, that exports all private profiles from database to file 'Privates.txt'
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class GetPrivatesCommand extends Command {

	@Override
	public void execute() {
		try {
			System.out.println("Starting export");
			FileIOWorker.getInstance().exportNames("Privates.txt", DaoUserProfiles.getDao().getPrivateProfiles());
		} catch (SQLException e) {
			System.err.println("An error has occured while exporting (" + e.getMessage() + ")");
		}
		System.out.println("Finished");

	}

	@Override
	public String executePhrase() {
		return "export-privates";
	}

	@Override
	public String description() {
		return "Exports private profiles into a file 'Privates.txt.'";
	}

}
