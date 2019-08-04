
package com.krupizde.persistence;

import com.krupizde.instagram.entities.Profile;
import java.io.File;
import java.util.ArrayList;
import java.sql.ResultSet;
import com.krupizde.downloader.Suffix;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import com.krupizde.instagram.entities.Medium;

/**
 * Dao class for working with mediums and medium_types in database. Uses
 * singleton,
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class DaoMedium {
	private DaoMedium() {
		super();
	}

	private static DaoMedium mDao = null;

	/**
	 * A singleton methos for returning instance of this class.
	 * 
	 * @return Only one instance of DaoMedium
	 */
	public static DaoMedium getDao() {
		if (mDao == null) {
			mDao = new DaoMedium();
		}
		return mDao;
	}

	/**
	 * Method tries to add medium to database. If it fails (unique name constraint
	 * is triggered) medium already exists in database.
	 * 
	 * @param medium Medium to add
	 * @return True if success, false otherwise
	 */
	public boolean addMedium(final Medium medium) {
		try {
			if (this.existsMediumByName(medium.getName())) {
				return false;
			}
			final int mediType = this.addMediumType(medium.getSuffix());
			final PreparedStatement stm = SqliteDatabase.getConn().prepareStatement(
					"insert into medium(id_medium_type, name, date_time_added, id_profile, deleted, link)values(?,?,?,?,?,?)");
			stm.setInt(1, mediType);
			stm.setString(2, medium.getName());
			stm.setDate(3, new Date(medium.getDateTimeAdded().getTime()));
			stm.setInt(4, DaoUserProfiles.getDao().getProfileId(medium.getProfile().getName()));
			stm.setBoolean(5, false);
			stm.setString(6, medium.getUrl());
			stm.executeUpdate();
		} catch (SQLException ex) {
		}
		return true;
	}

	/**
	 * Method checks if given medium type is already in database. If so, its id is
	 * returned. New medium type is inserted otherwise and its id is returned.
	 * 
	 * @param suff Suffix of the medium type to insert/get id of
	 * @return Id of medium type
	 * @throws SQLException
	 */
	public int addMediumType(final Suffix suff) throws SQLException {
		int suffId = this.typeId(suff);
		if (suffId != -1) {
			return suffId;
		}
		final PreparedStatement stm = SqliteDatabase.getConn()
				.prepareStatement("insert into media_type(name,short)values(?,?)");
		stm.setString(1, (suff == Suffix.JPG) ? "Picture" : "Video");
		stm.setString(2, suff.name().toLowerCase());
		stm.executeUpdate();
		suffId = this.typeId(suff);
		return suffId;
	}

	/**
	 * Method tries to get id of given medium type
	 * 
	 * @param suff Suffix of which to return id
	 * @return Id of medium type if exists, -1 otherwise
	 * @throws SQLException
	 */
	private int typeId(final Suffix suff) throws SQLException {
		final PreparedStatement stm = SqliteDatabase.getConn()
				.prepareStatement("select id_media_type from media_type where short = ?");
		stm.setString(1, suff.name().toLowerCase());
		final ResultSet set = stm.executeQuery();
		if (set.next()) {
			return set.getInt(1);
		}
		return -1;
	}

	/**
	 * Method checks if medium with given name already exists in the database.
	 * 
	 * @param name Name of the medium to check
	 * @return True if it exists, false otherwise
	 * @throws SQLException
	 */
	public boolean existsMediumByName(final String name) throws SQLException {
		final PreparedStatement stm = SqliteDatabase.getConn().prepareStatement("select * from medium where name = ?");
		stm.setString(1, name);
		final ResultSet set = stm.executeQuery();
		return set.next();
	}

	/**
	 * Method sets download time to given medium (now)
	 * 
	 * @param m Medium to which set the download time
	 */
	public void setDownTime(final Medium m) {
		try {
			final PreparedStatement stm = SqliteDatabase.getConn()
					.prepareStatement("update medium set date_time_downloaded = ? where name = ?");
			stm.setDate(1, new Date(new java.util.Date().getTime()));
			stm.setString(2, m.getName());
			stm.executeUpdate();
		} catch (SQLException ex) {
		}
	}

	/**
	 * Method returns limit number of mediums from database that have not set
	 * download time (were not downoaded yet).
	 * 
	 * @param limit Limit of returned mediums
	 * @return List of loaded mediums
	 * @throws SQLException
	 */
	public ArrayList<Medium> getNotDownloadedMediums(int limit) throws SQLException {
		final ArrayList<Medium> temp = new ArrayList<Medium>();
		final PreparedStatement stm = SqliteDatabase.getConn().prepareStatement(
				"select disk_path, person.name, profile.name, medium.name, id_medium, link, short, date_time_added, api_deleted from medium join media_type on medium.id_medium_type = media_type.id_media_type join profile on profile.id_profile = medium.id_profile join person on person.id_person = profile.id_person where is_valid = true and api_deleted = false and date_time_downloaded is null order by person.download_priority asc limit ?");
		stm.setInt(1, limit);
		final ResultSet set = stm.executeQuery();
		while (set.next()) {
			final PreparedStatement stm2 = SqliteDatabase.getConn().prepareStatement(
					"select person.name, count(person.id_person) from profile join person on profile.id_person = person.id_person where person.name = ? group by person.name");
			stm2.setString(1, set.getString(2));
			final ResultSet set2 = stm2.executeQuery();
			int pocet = 1;
			if (set2.next()) {
				pocet = set2.getInt(2);
			}
			final String path = String
					.valueOf(set.getString(1).replace("\\", File.separator).replace("/", File.separator))
					+ ((pocet == 1) ? "" : (String.valueOf(File.separator) + set.getString(3)));
			temp.add(new Medium(set.getString(4), set.getString(6), path, 0L,
					Suffix.valueOf(set.getString(7).toUpperCase()), (java.util.Date) set.getDate(8), (Profile) null));
		}
		return temp;
	}
	
	public void setApiDeleted(final Medium medium) throws SQLException {
		final PreparedStatement stm = SqliteDatabase.getConn().prepareStatement("update medium set api_deleted = true where name = ?");
		stm.setString(1, medium.getName());
		stm.executeUpdate();
	}

}
