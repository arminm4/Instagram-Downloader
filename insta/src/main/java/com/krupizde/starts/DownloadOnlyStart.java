package com.krupizde.starts;

import com.krupizde.main.App;
import com.krupizde.main.DownloadFromDatabase;

/**
 * Class represents Starting method, where only downloading is started
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class DownloadOnlyStart extends Start {

	@Override
	public void start() {
		System.out.println("Starting download only start");
		try {
			App.instagram();
		} catch (Exception e) {
		}
		final DownloadFromDatabase down = new DownloadFromDatabase();
		down.start();
		try {
			down.join();
		} catch (InterruptedException e) {
		}
		System.out.println("FINISHED");
	}

	@Override
	public String startParam() {
		return "-d";
	}

	@Override
	public String description() {
		return "Will download all undownloaded mediums from database in only one iteration";
	}

	@Override
	public String name() {
		return "Download only start";
	}

}
