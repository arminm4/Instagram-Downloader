
package com.krupizde.persistence;

import java.sql.ResultSet;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.krupizde.instagram.entities.Profile;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Dao class for working with profiles and users in database. Uses a singleton
 * 
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class DaoUserProfiles {
	private static DaoUserProfiles dao;

	static {
		DaoUserProfiles.dao = null;
	}

	private DaoUserProfiles() {
		super();
	}

	/**
	 * A singleton methos for returning instance of this class.
	 * 
	 * @return Only one instance of DaoUserProfiles
	 */
	public static DaoUserProfiles getDao() {
		if (DaoUserProfiles.dao == null) {
			DaoUserProfiles.dao = new DaoUserProfiles();
		}
		return DaoUserProfiles.dao;
	}

	/**
	 * Method deletes all profiles from database, that have non_valid flag
	 */
	public void deleteNonValids() {
		try {
			final PreparedStatement stm = SqliteDatabase.getConn()
					.prepareStatement("delete from profile where is_valid = false");
			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method iterates loaded users from given file and inserts them into database.
	 * 
	 * @param file Path to file from which to load profile names
	 * @param path Path to root folder where to save all mediums of loaded profiles
	 */
	public void addUsersAndProfiles(final String file, final String path) {
		final ArrayList<String> strings = (ArrayList<String>) FileIOWorker.getInstance().namesByLine(file).get();
		for (String s : strings) {
			String tmpPath = String.valueOf(path) + File.separator;
			s = s.trim();
			if (s.contains(" ")) {
				int first = s.indexOf(" ");
				if (s.lastIndexOf(" ") != first) {
					s.replaceAll(" ", "");
				}
				final String[] split = { s.substring(0, first), s.substring(first, s.length()) };
				tmpPath = String.valueOf(tmpPath) + split[0].trim();
				int id = 0;
				try {
					id = this.addPerson(split[0].trim(), tmpPath);
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				String[] split2;
				for (int length = (split2 = split[1].split(",")).length, i = 0; i < length; ++i) {
					final String str = split2[i];
					final String profile = str.trim();
					try {
						this.addProfile(profile, id);
					} catch (SQLException ex2) {
					}
				}
			} else {
				tmpPath = String.valueOf(tmpPath) + s;
				try {
					this.addProfile(s, this.addPerson(s, tmpPath));
				} catch (SQLException ex3) {
				}
			}
		}
	}

	/**
	 * Sets private flag to given profile in database
	 * 
	 * @param p Profile to which to set the flag
	 * @throws SQLException
	 */
	public void setPrivate(Profile p) throws SQLException {
		PreparedStatement stm = SqliteDatabase.getConn()
				.prepareStatement("update profile set is_private = true where name = ?");
		stm.setString(1, p.getName());
		stm.executeUpdate();
	}

	/**
	 * @return List of profiles which have private flag set to true
	 * @throws SQLException
	 */
	public List<Profile> getPrivateProfiles() throws SQLException {
		final ArrayList<Profile> temp = new ArrayList<Profile>();
		final PreparedStatement stm = SqliteDatabase.getConn().prepareStatement(
				"select profile.name, person.name, disk_path, id_profile, is_valid, is_private, full_finished from profile join person on person.id_person = profile.id_person where is_valid = true and is_private = true");
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
			temp.add(new Profile(set.getInt("id_profile"), set.getString(1), path, set.getBoolean("is_valid"),
					set.getBoolean("is_private"), set.getBoolean("full_finished")));
		}
		return temp;
	}

	/**
	 * Sets full_finished flag of given profile to given value
	 * 
	 * @param p        User to which set the flag
	 * @param finished Values to which set the flag
	 * @throws SQLException
	 */
	public void setFinished(Profile p, boolean finished) throws SQLException {
		PreparedStatement stm = SqliteDatabase.getConn()
				.prepareStatement("update profile set full_finished = ? where name = ?");
		stm.setBoolean(1, finished);
		stm.setString(2, p.getName());
		stm.executeUpdate();
	}

	/**
	 * Method adds a person to database (not a profile)
	 * 
	 * @param name     Name of the person
	 * @param diskPath Disk path where to save it's profile's mediums
	 * @return Id of this person from database
	 * @throws SQLException
	 */
	private int addPerson(final String name, final String diskPath) throws SQLException {
		int id = this.getPersonId(name);
		if (id != 0) {
			return id;
		}
		final PreparedStatement stm = SqliteDatabase.getConn()
				.prepareStatement("insert into Person(name, disk_path)values(?,?)");
		stm.setString(1, name);
		stm.setString(2, diskPath);
		stm.executeUpdate();
		return this.getPersonId(name);
	}

	/**
	 * Method loads number of loaded posts of given profile
	 * 
	 * @param p Profile to which to load number of posts
	 * @return Number of loaded posts of given profile
	 * @throws SQLException
	 */
	public int getNumOfLoadedPosts(Profile p) throws SQLException {
		PreparedStatement stm = SqliteDatabase.getConn().prepareStatement(
				"select profile.id_profile, count(id_medium) from medium join profile on profile.id_profile = medium.id_profile"
						+ " where profile.name = ? group by profile.id_profile");
		stm.setString(1, p.getName());
		ResultSet set = stm.executeQuery();
		while (set.next()) {
			return set.getInt(2);
		}
		return 0;
	}

	/**
	 * Method adds a profile to database
	 * 
	 * @param name   Name of the profile
	 * @param idPerson Which person is owner
	 * @throws SQLException
	 */
	private void addProfile(final String name, final int idPerson) throws SQLException {
		final PreparedStatement stm = SqliteDatabase.getConn()
				.prepareStatement("insert into profile(name, is_valid, id_person)values(?,?,?)");
		stm.setString(1, name);
		stm.setBoolean(2, true);
		stm.setInt(3, idPerson);
		stm.executeUpdate();
	}

	/**
	 * Method loads and returns list of profiles that have set flag is_valid to
	 * false
	 * 
	 * @return List of non-valid profiles
	 * @throws SQLException
	 */
	public ArrayList<Profile> getNonValids() throws SQLException {
		final ArrayList<Profile> temp = new ArrayList<Profile>();
		final PreparedStatement stm = SqliteDatabase.getConn().prepareStatement(
				"select profile.name, person.name, disk_path, id_profile, is_valid, is_private, full_finished from profile join person on person.id_person = profile.id_person where is_valid = false");
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
			temp.add(new Profile(set.getInt("id_profile"), set.getString(1), path, set.getBoolean("is_valid"),
					set.getBoolean("is_private"), set.getBoolean("full_finished")));
		}
		return temp;
	}

	/**
	 * Method loads and returns list of profiles that have set flag is_valid to true
	 * 
	 * @return List of valid profiles
	 * @throws SQLException
	 */
	public ArrayList<Profile> getValidProfiles() throws SQLException {
		final ArrayList<Profile> temp = new ArrayList<Profile>();
		final PreparedStatement stm = SqliteDatabase.getConn().prepareStatement(
				"select profile.name, person.name, disk_path, id_profile, is_valid, is_private, full_finished from profile join person on person.id_person = profile.id_person where is_valid = true");
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
			temp.add(new Profile(set.getInt("id_profile"), set.getString(1), path, set.getBoolean("is_valid"),
					set.getBoolean("is_private"), set.getBoolean("full_finished")));
		}
		return temp;
	}

	/**
	 * Method loads and returns list of all profiles from database
	 * 
	 * @return List of all profiles
	 * @throws SQLException
	 */
	public ArrayList<Profile> getAllProfiles() throws SQLException {
		final ArrayList<Profile> temp = new ArrayList<Profile>();
		final PreparedStatement stm = SqliteDatabase.getConn().prepareStatement(
				"select profile.name, person.name, disk_path, id_profile, is_valid, is_private, full_finished from profile join person on person.id_person = profile.id_person");
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
			temp.add(new Profile(set.getInt("id_profile"), set.getString(1), path, set.getBoolean("is_valid"),
					set.getBoolean("is_private"), set.getBoolean("full_finished")));
		}
		return temp;
	}

	/**
	 * Method returns database if of person with given name
	 * 
	 * @param name Name of the person you want to load id
	 * @return Id of person with given name
	 * @throws SQLException
	 */
	public int getPersonId(final String name) throws SQLException {
		final PreparedStatement stm = SqliteDatabase.getConn()
				.prepareStatement("select id_person from person where name = ?");
		stm.setString(1, name);
		final ResultSet set = stm.executeQuery();
		while (set.next()) {
			return set.getInt(1);
		}
		return 0;
	}

	/**
	 * Method returns database if of profile with given name
	 * 
	 * @param name Name of the profile you want to load id
	 * @return Id of profile with given name
	 * @throws SQLException
	 */
	public int getProfileId(final String name) throws SQLException {
		final PreparedStatement stm = SqliteDatabase.getConn()
				.prepareStatement("select id_person from profile where name = ?");
		stm.setString(1, name);
		final ResultSet set = stm.executeQuery();
		while (set.next()) {
			return set.getInt(1);
		}
		return 0;
	}

	/**
	 * Method sets flag is_valid to false in database to given profile
	 * 
	 * @param profile Profile to set the flag
	 */
	public void nonValid(final Profile profile) {
		try {
			final PreparedStatement stm = SqliteDatabase.getConn()
					.prepareStatement("update profile set is_valid = false where profile.name = ?");
			stm.setString(1, profile.getName());
			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method sets flag is_valid to true in database to given profile
	 * 
	 * @param user User to set the flag
	 */
	public void validAgain(final Profile user) {
		try {
			final PreparedStatement stm = SqliteDatabase.getConn()
					.prepareStatement("update profile set is_valid = true where profile.name = ?");
			stm.setString(1, user.getName());
			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
