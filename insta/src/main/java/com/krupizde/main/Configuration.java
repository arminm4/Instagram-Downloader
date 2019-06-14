
package com.krupizde.main;

/**
 * Class represents Configuration object containing all configuration settings.
 * Only one instance is in whole application.
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class Configuration {
	private String username;
	private String password;
	private String savePath;
	private String namesPath;
	private int timeout;
	private static Configuration config;

	/**
	 * Private constructor for singleton purposes
	 */
	private Configuration() {
		super();
	}

	/**
	 * Method returns only one instantce of configuration class
	 * 
	 * @return
	 */
	public static Configuration getConfig() {
		if (Configuration.config == null) {
			Configuration.config = new Configuration();
		}
		return Configuration.config;
	}

	public String getUsername() {
		return this.username;
	}

	public Configuration setUsername(final String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return this.password;
	}

	public Configuration setPassword(final String password) {
		this.password = password;
		return this;
	}

	public String getSavePath() {
		return this.savePath;
	}

	public Configuration setSavePath(final String savePath) {
		this.savePath = savePath;
		return this;
	}

	public String getNamesPath() {
		return this.namesPath;
	}

	public Configuration setNamesPath(final String namesPath) {
		this.namesPath = namesPath;
		return this;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public Configuration setTimeout(final int timeout) {
		this.timeout = timeout;
		return this;
	}
}
