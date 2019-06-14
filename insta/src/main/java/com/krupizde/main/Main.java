
package com.krupizde.main;

import com.krupizde.persistence.SqliteDatabase;

/**
 * Main class of the application
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class Main {
	public Main() {
		super();
	}

	/**
	 * Method sets shutdown hook to commit changes to database and close connection
	 * and starts the application TUI facade.
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				SqliteDatabase.close();
			}
		}, "Shutdown-thread"));
		final App app = new App();
		//args[0] = "-i";
		app.run(args);
	}
}
