
package com.krupizde.main;

import com.krupizde.instagram.entities.Medium;
import com.krupizde.downloader.Suffix;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import org.brunocvcunha.instagram4j.requests.payload.InstagramCarouselMediaItem;
import java.util.Date;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.InstagramUserFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.brunocvcunha.instagram4j.requests.InstagramRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import com.krupizde.instagram.entities.Profile;
import com.krupizde.persistence.DaoMedium;
import java.sql.SQLException;
import com.krupizde.persistence.DaoUserProfiles;

/**
 * Class represents separate thread for crawnling through instagram profiles and
 * their posts. It is a separate thread for future paralelization with
 * downloading and other actions.
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class InstagramCrawler extends Thread {
	public InstagramCrawler() {
		super();
	}

	/**
	 * Main thread method that loads profiles from database, and iterates through
	 * them. Each profile is searched through api and their feed is returned.
	 * Because of instagram api pagination there are multiple feed searches executed
	 * with next max_post_id. If there is already medium with returned id in the
	 * database, feed loading for given user is stoped and next user is being
	 * loaded. If fullFinished flag of user is false (application was interupter or
	 * profile wasn't loaded yet) and there are already some posts of this profile
	 * in database, loading of this profile's feed is started over again, because
	 * that is a flag that the application was interrupted in the middle of loading
	 * this profile's posts and not all of them were loaded.
	 */
	public void run() {
		InstagramSearchUsernameResult userResult = null;
		final DaoUserProfiles uDao = DaoUserProfiles.getDao();
		final DaoMedium mDao = DaoMedium.getDao();
		List<Profile> profiles = loadProfiles(uDao);
		if (profiles == null)
			return;
		System.out.println("Starting crawling");
		for (final Profile profile : profiles) {
			try {
				userResult = (InstagramSearchUsernameResult) App.instagram()
						.sendRequest((InstagramRequest<?>) new InstagramSearchUsernameRequest(profile.getName()));
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			if ("fail".equals(userResult.getStatus())) {
				System.out.println("\n-----------> " + profile.getName() + " is non-valid profile <------------\n");
				uDao.nonValid(profile);
			} else {
				try {
					int medCount = uDao.getNumOfLoadedPosts(profile);
					if (userResult.getUser().is_private()) {
						uDao.setPrivate(profile);
					}
					if (!profile.isFullFinished() && medCount != 0) {
						System.out.println("\n-----------> " + profile.getName()
								+ " didn´t finish loading last time, starting over <------------\n");
					}
					System.out.println();
					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Loading " + profile.getName()
							+ " posts <<<<<<<<<<<<<<<<<<<<<<<<<<<");
					System.out.println();
					InstagramFeedResult tagFeed = (InstagramFeedResult) App.instagram().sendRequest(
							(InstagramRequest<?>) new InstagramUserFeedRequest(userResult.getUser().getPk()));
					tagFeed.setAuto_load_more_enabled(true);
					for (boolean down = true; down
							&& tagFeed.isMore_available(); tagFeed = (InstagramFeedResult) App.instagram().sendRequest(
									(InstagramRequest<?>) new InstagramUserFeedRequest(userResult.getUser().getPk(),
											tagFeed.getNext_max_id(), 0L))) {
						System.out.println(">>> Got " + tagFeed.getNum_results() + " results of " + profile.getName());
						for (final InstagramFeedItem feedResult : tagFeed.getItems()) {
							if (feedResult.getMedia_type() == 8) {
								if (!addMultiPost(feedResult, mDao, profile)) {
									down = false;
									break;
								}
							} else {
								if (!addNormalPost(feedResult, mDao, profile)) {
									down = false;
									break;
								}
							}
						}
					}
				} catch (Exception e2) {
					System.err.println("An error has occured");
					e2.printStackTrace();
				}
				try {
					uDao.setFinished(profile, true);
				} catch (SQLException e) {
				}
			}
			waitAMoment();
			System.gc();
		}
	}

	/**
	 * Method loads all profiles from database and returns them as a list
	 * 
	 * @param uDao Dao object for profiles
	 * @return List of profiles from database if possible. Null otherwise.
	 */
	private List<Profile> loadProfiles(DaoUserProfiles uDao) {
		ArrayList<Profile> profiles = null;
		try {
			profiles = (ArrayList<Profile>) uDao.getValidProfiles();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return profiles;
	}

	/**
	 * Method to load and save to database a normal picture/video post that
	 * InstagramFeedResult returned.
	 * 
	 * @param feedResult The post InstagramFeedResult returned
	 * @param mDao       Dao object for saving the picture/video of this post
	 * @param profile    Profile of which the post is part of
	 * @return True if post was succesfully saved, false if not (post id is already
	 *         saved)
	 */
	private boolean addNormalPost(InstagramFeedItem feedResult, DaoMedium mDao, Profile profile) {
		String url = extractMediaUrl(feedResult),
				name = new StringBuilder(String.valueOf(feedResult.getPk())).toString();
		Suffix suff;
		final Date created = getDateCreated(feedResult);

		suff = ((feedResult.getMedia_type() == 1) ? Suffix.JPG : Suffix.MP4);
		if (!mDao.addMedium(new Medium(name, url, "", 0L, suff, created, profile)) && profile.isFullFinished()) {
			return false;
		}
		return true;
	}

	/**
	 * Method tu extract media URL from InstagramFeedItem
	 * 
	 * @param feedResult Feed result from which to extract url
	 * @return URL of the medium
	 */
	@SuppressWarnings("unchecked")
	public static String extractMediaUrl(InstagramFeedItem feedResult) {
		String url = "";
		if (feedResult.getMedia_type() == 1) {
			url = (String) ((LinkedHashMap<?, ?>) ((ArrayList<Object>) feedResult.image_versions2.get("candidates"))
					.get(0)).get("url");
		} else {
			url = (String) (((LinkedHashMap<?, ?>) feedResult.getVideo_versions().get(0)).get("url"));
		}
		return url;
	}

	/**
	 * Method to save all pictures/photos from one multi-post.
	 * 
	 * @param feedResult The multi-post InstagramFeedResult returned
	 * @param mDao       Dao object for saving all the pictures/videos of this
	 *                   multi-post
	 * @param profile    Profile of which the multi-post is part of
	 * @return
	 */
	private boolean addMultiPost(InstagramFeedItem feedResult, DaoMedium mDao, Profile profile) {
		String url = "";
		final Date created = getDateCreated(feedResult);
		for (final InstagramCarouselMediaItem i : feedResult.getCarousel_media()) {
			url = extractMediaUrl(i);
			String name = new StringBuilder(String.valueOf(i.getPk())).toString();
			Suffix suff = ((i.getMedia_type() == 1) ? Suffix.JPG : Suffix.MP4);
			if (!mDao.addMedium(new Medium(name, url, "", 0L, suff, created, profile)) && profile.isFullFinished()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method extracts date of the post creation from InstagramFeedItem
	 * 
	 * @param feedResult The post InstagramFeedResult returned
	 * @return Date of the post creation. If not possible, returns default time
	 *         (1.1.1970)
	 */
	private Date getDateCreated(InstagramFeedItem feedResult) {
		long l;
		try {
			l = ((Integer) feedResult.caption.get("created_at_utc")).longValue();
		} catch (Exception e3) {
			l = 0L;
		}
		l *= 1000L;
		return new Date(l);
	}

	/**
	 * Puts crawling thread to sleep for 150 + <0,150) miliseconds
	 */
	private void waitAMoment() {
		try {
			Thread.sleep(150 + new Random().nextInt(150));
		} catch (InterruptedException e) {
		}
	}
}
