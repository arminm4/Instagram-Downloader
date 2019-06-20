package com.krupizde.starts;



import com.krupizde.main.App;
import com.krupizde.main.Configuration;
import com.krupizde.main.DownloadFromDatabase;
import com.krupizde.main.InstagramCrawler;
import com.krupizde.persistence.DaoUserProfiles;

/**
 * Class represents Starting method with standard iteration.
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class DiscreteStart extends Start {

	@Override
	public void start() {
		System.out.println("Starting application in discrete mode");
		try {
			App.instagram();
		} catch (Exception e) {
			System.out.println("An error has occured, exiting ("+e.getMessage()+")");
			return;
		}
		while (true) {
			System.out.println("Loading users from " + Configuration.getConfig().getNamesPath());
			DaoUserProfiles.getDao().addUsersAndProfiles(Configuration.getConfig().getNamesPath(),
					Configuration.getConfig().getSavePath());
			final InstagramCrawler ic = new InstagramCrawler();
			final DownloadFromDatabase down = new DownloadFromDatabase();
			ic.start();
			try {
				ic.join();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			down.start();
			try {
				down.join();
			} catch (InterruptedException ex2) {
			}
			try {
				System.err.println("Sleepig for " + Configuration.getConfig().getTimeout() + " miliseconds");
				Thread.sleep(Configuration.getConfig().getTimeout());
			} catch (InterruptedException ex3) {
			}
		}

	}

	@Override
	public String startParam() {
		return "-di";
	}

	@Override
	public String description() {
		return "Starts application in discrete mode -\n\t\t\t\t1) loads profile names from set file and inserts them into database\n"
				+ "\t\t\t\t2) starts crawling profiles loaded from database and inserting their posts into database\n"
				+ "\t\t\t\t3) starts downloading mediums from database (video links have to be refreshed)";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Discrete start";
	}

}
