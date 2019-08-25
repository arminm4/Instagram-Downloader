/**
 * 
 */
package com.krupizde.command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import com.krupizde.instagram.entities.Profile;
import com.krupizde.persistence.DaoUserProfiles;

/**
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class SetPriorityCommand extends Command {

	@Override
	public void execute() {
		String comm = "";
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println(
				"\nTo stop setting priorities type 'exit'.\nTo set priority to a profile type 'profile_name' 'priority'."
				+ " To list all profiles type 'list'.\nTo list one profile type 'list' <profile name>");
		do {
			comm = sc.nextLine();
			if ("exit".equals(comm))
				break;
			else if ("list".equals(comm)) {
				ArrayList<Profile> profiles = null;
				try {
					profiles = (ArrayList<Profile>) DaoUserProfiles.getDao().getAllProfiles();
					Collections.sort(profiles);
				} catch (SQLException e) {
					System.err.println("Cannot list profiles");
				}
				if(profiles.isEmpty()) {
					System.out.println("NONE");
					continue;
				}
				for (final Profile p : profiles) {
					System.out.println(String.valueOf(p.getName()) + "\t" + (p.isValid() ? "Valid" : "Non-valid") + "\t"
							+ p.getPriority());
				}
				System.out.println(">>>>>>>>>DONE<<<<<<<<<");
				continue;
			} else if (comm.matches("list .*")) {				
				ArrayList<Profile> list = null;
				try {
					list = DaoUserProfiles.getDao().getProfilesByName(comm.trim().split(" ")[1]);
				} catch (SQLException e) {
					System.err.println("Cannot list profiles");
				}
				if(list.isEmpty()) {
					System.out.println("NONE");
					continue;
				}
				for (Profile p : list) {
					System.out.println(String.valueOf(p.getName()) + "\t" + (p.isValid() ? "Valid" : "Non-valid") + "\t"
							+ p.getPriority());
				}
				System.out.println(">>>>>>>>>DONE<<<<<<<<<");
				continue;
			}
			String[] params = comm.split(" ");
			if (params.length != 2) {
				continue;
			}
			try {
				DaoUserProfiles.getDao().setPriority(params[0], Integer.parseInt(params[1]));
				ArrayList<Profile> updates = DaoUserProfiles.getDao().getProfilesByName(params[0]);
				for (Profile p : updates) {
					System.out.println("Set priority " + params[1] + " to " + p.getName());
				}
			} catch (NumberFormatException e) {
				System.err.println("Wrong input of " + params[1] + ". Number expected.");
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("Profile " + params[0] + " not found.");
			}
		} while (true);
		System.out.println("Exitting");
	}

	@Override
	public String executePhrase() {
		return "priority";
	}

	@Override
	public String description() {
		return "Allows user to set priorities as number 1 - 10000 to multiple profiles, which will prioritize their mediums in downloading iteration.";
	}

}
