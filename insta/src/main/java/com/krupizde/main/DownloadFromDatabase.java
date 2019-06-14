
package com.krupizde.main;

import java.util.ArrayList;

import org.brunocvcunha.instagram4j.requests.payload.InstagramGetMediaInfoResult;

import com.krupizde.downloader.SpeedObject;
import com.krupizde.downloader.Suffix;
import com.krupizde.downloader.IDownloadItem;
import com.krupizde.instagram.entities.Medium;
import com.krupizde.downloader.Downloader;

import java.sql.SQLException;
import com.krupizde.persistence.DaoMedium;
import com.krupizde.requsts.CustomGetRequest;

/**
 * Class represents downloading of mediums from database. It is created as a
 * Thread for possible future paralelization with instagram crawling
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class DownloadFromDatabase extends Thread {
	public DownloadFromDatabase() {
		super();
	}

	/**
	 * Main method of the thread. Method iterated through mediums from database.
	 * There is a thousand mediums loaded every iteration and all of them are then
	 * downloaded to set folder.
	 */
	public void run() {
		final DaoMedium mDao = DaoMedium.getDao();
		System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>> STARTING DOWNLOADING <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
		ArrayList<Medium> temp = null;
		int limit = 1000;
		try {
			temp = (ArrayList<Medium>) mDao.getNotDownloadedMediums(limit);
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("An error has occured while downloading, exiting");
			return;
		}
		final Downloader d = new Downloader();
		do {
			for (final Medium m : temp) {
				download(m, d);
			}
			try {
				temp = (ArrayList<Medium>) mDao.getNotDownloadedMediums(limit);
			} catch (SQLException e) {
			}
		} while (temp.size() == 1000);
		for (final Medium m : temp) {
			download(m, d);
		}
	}

	/**
	 * Method downloads given medium (calls method from Downloader). If the medium
	 * is a video, url must be refreshed (instagram has time limited urls for video
	 * posts).
	 * 
	 * @param m Medium to download
	 * @param d Downloader which is used to download
	 */
	private void download(Medium m, Downloader d) {
		try {
			if (m.getSuffix() == Suffix.MP4) {
				System.out.println("Gotta keep them video url fresh");
				InstagramGetMediaInfoResult res = App.instagram()
						.sendRequest(new CustomGetRequest(Long.parseLong(m.getName())));
				m.setUrl(InstagramCrawler.extractMediaUrl(res.getItems().get(0)));
			}
			System.out.println("Downloading " + m.getSuffix());
			d.downloadMedium((IDownloadItem) m, (SpeedObject) null);
			DaoMedium.getDao().setDownTime(m);
		} catch (IllegalAccessException e2) {
			System.err.println("An error has occured (" + e2.getMessage() + ")");
		} catch (Exception e) {
			System.err.println("An error has occured (" + e.getMessage() + ")");
		}
	}
}
