package com.krupizde.starts;

import com.krupizde.main.App;
import com.krupizde.main.Configuration;
import com.krupizde.main.InstagramCrawler;
import com.krupizde.persistence.DaoUserProfiles;
/**
 * Class represents Starting method, with iteration cycle 
 * 1) Loads names from set file to database
 * 2) Starts crawling instagram profiles
 * 3) Sleeps set time from config.txt
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class CrawlingOnlyStart extends Start{

	@Override
	public void start() {
		System.out.println("Starting crawling only mode");
		try {
			App.instagram();
		} catch (Exception e) {
		}
		while(true) {
			System.out.println("Loading users from " + Configuration.getConfig().getNamesPath());
			DaoUserProfiles.getDao().addUsersAndProfiles(Configuration.getConfig().getNamesPath(),
					Configuration.getConfig().getSavePath());
			final InstagramCrawler ic = new InstagramCrawler();
			ic.start();
			try {
				ic.join();
				System.out.println("Waiting " + Configuration.getConfig().getTimeout() + " miliseconds");
				Thread.sleep(Configuration.getConfig().getTimeout());
			} catch (InterruptedException e) {
			}
		}
		
	}

	@Override
	public String startParam() {
		return "-c";
	}

	@Override
	public String description() {
		return "Application will first load profile names from set file and then start crawling profiles in infinite loop";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Crawling only start";
	}

}
