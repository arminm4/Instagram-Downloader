
package com.krupizde.main;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import java.util.Properties;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import java.sql.SQLException;
import com.krupizde.persistence.SqliteDatabase;
import com.krupizde.starts.StartsManager;

import org.brunocvcunha.instagram4j.Instagram4j;

/**
 * Class represents facade of the application. Instance of this class runs in
 * the main Thread and manages starting of crawling and downloading in their
 * separate Threads.
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class App {
	private static Instagram4j insta;

	/**
	 * Basic auto-generated constructor
	 */
	public App() {
		super();
	}
	/** 
	 * Maint method of this Facade. Based on input parameters
	 * @param args User input arguments
	 */
	public void run(final String[] args) {
		StartsManager manager = StartsManager.getManager();
		if(args.length != 0) {
			manager.start(args[0]);
		}else {
			manager.start("");
		}
	}

	/**
	 * Method works like a singleton for Instagram connection. First configuration file is loaded and database connection is opened.
	 * If any of these fails, application exits with exit code 2.
	 * @return Only one instance of Instagram4j class
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static Instagram4j instagram() throws ClientProtocolException, IOException {
		if (App.insta == null) {
			if (!loadConfig()) {
				System.exit(2);
			}
			try {
				SqliteDatabase.getConn();
			} catch (SQLException e3) {
				System.err.println("Database file not found, exiting");
				System.exit(2);
			}
			(App.insta = Instagram4j.builder().username(Configuration.getConfig().getUsername())
					.password(Configuration.getConfig().getPassword()).build()).setup();
			App.insta.login();
		}
		return App.insta;
	}

	/**
	 * Method loads configuration file and sets loaded information into singleton instantion of Configuration.
	 * @return True if configurations are loaded, false otherwise
	 */
	private static boolean loadConfig() {
		final Properties prop = new Properties();
		String username = "";
		String password = "";
		String savePath = "";
		String namesPath = "";
		int timeout = 0;
		try {
			System.out.println("Loading file config.txt");
			prop.load(new FileInputStream(new File("config.txt")));
		} catch (FileNotFoundException e) {
			System.err.println("File not found, exiting");
			return false;
		} catch (IOException e2) {
			System.err.println("An error has occured, exiting");
			return false;
		}
		savePath = prop.getProperty("savePath");
		namesPath = prop.getProperty("namesPath");
		if (savePath.isEmpty()) {
			System.err.println("Save path not set, downloading to local folder");
			final File saves = new File("saves");
			if (!saves.exists()) {
				saves.mkdir();
			}
			savePath = saves.getAbsolutePath();
		}
		if (namesPath.isEmpty()) {
			System.err.println("Names path not set, looking for local file");
			namesPath = "names.txt";
			final File names = new File("names.txt");
			if (!names.exists()) {
				System.err.println("Local file not found, exiting");
				return false;
			}
		}
		username = prop.getProperty("username");
		password = prop.getProperty("password");
		if (username.isEmpty() || password.isEmpty()) {
			System.err.println("Username or password not set, exiting");
			return false;
		}
		try {
			timeout = Integer.parseInt(prop.getProperty("timeout", "60000"));
		} catch (NumberFormatException e3) {
			timeout = 60000;
		}
		Configuration.getConfig().setNamesPath(namesPath).setPassword(password).setSavePath(savePath)
				.setUsername(username).setTimeout(timeout);
		return true;
	}
}
