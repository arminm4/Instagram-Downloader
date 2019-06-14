
package com.krupizde.command;

import java.sql.SQLException;
import com.krupizde.persistence.SqliteDatabase;

/**
 * Class represents command, that deletes all data from database
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class ResetDatabase extends Command {
	public ResetDatabase() {
		super();
	}

	public void execute() {
		try {
			System.out.println("Deleting data from database");
			SqliteDatabase.resetDatabase();
			System.out.println("Data deleted");
		} catch (SQLException e) {
			System.err.println("Could not delete data from database (" + e.getMessage() + ")");
		}
	}

	public String executePhrase() {
		return "rebuild-database";
	}

	public String description() {
		return "Deletes all data from database";
	}
}
