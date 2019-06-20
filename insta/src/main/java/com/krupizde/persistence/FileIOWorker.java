
package com.krupizde.persistence;

import com.krupizde.instagram.entities.Profile;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class for executing necessary IO file actions. Work like a singleton, so only
 * one instance of this class exists in the whole application
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class FileIOWorker {
	private static FileIOWorker loader;

	static {
		FileIOWorker.loader = null;
	}

	private FileIOWorker() {
		super();
	}

	/**
	 * Method that return only one instance of FileIOWorker for singleton
	 * 
	 * @return Only one instance of this class
	 */
	public static FileIOWorker getInstance() {
		if (FileIOWorker.loader == null) {
			FileIOWorker.loader = new FileIOWorker();
		}
		return FileIOWorker.loader;
	}

	/**
	 * Method loads file set in configuration file (or default one) and loads
	 * profiles line by line or in some cases in format <br>
	 * 'PersonName' 'profileName', 'profileName', ... ,'profileName'\n
	 * 
	 * @param fileDest Path to file from which to load profiles
	 * @return Optional list of string each of them represents profile or person and
	 *         it's profiles
	 */
	public Optional<List<String>> namesByLine(final String fileDest) {
		final List<String> temp = new ArrayList<String>();
		final File f = new File(fileDest);
		if (!f.exists() || !f.canRead()) {
			return Optional.empty();
		}
		String line = "";
		BufferedReader stream = null;
		try {
			stream = new BufferedReader(new FileReader(f));
			while ((line = stream.readLine()) != null) {
				temp.add(line);
			}
		} catch (IOException e) {
			return Optional.empty();
		} finally {
			try {
				stream.close();
			} catch (IOException ex) {
			}
		}
		try {
			stream.close();
		} catch (IOException ex2) {
		}
		try {
			final PrintWriter writer = new PrintWriter(f);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException ex3) {
		}
		return Optional.of(temp);
	}

	/**
	 * Exports profiles to given file. If file doesn't exist, it is create
	 * 
	 * @param file     Path to file in which profiles are to be exported
	 * @param profiles List of profiles to export
	 */
	public void exportNames(final String file, final List<Profile> profiles) {
		final File f = new File(file);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException ex) {
			}
		}
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (final Profile p : profiles) {
			writer.println(p.getName());
		}
		writer.close();
	}
}
