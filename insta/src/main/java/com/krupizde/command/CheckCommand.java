
package com.krupizde.command;

import java.util.ArrayList;
import org.brunocvcunha.instagram4j.requests.InstagramRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import com.krupizde.main.App;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import com.krupizde.instagram.entities.Profile;
import java.sql.SQLException;
import com.krupizde.persistence.DaoUserProfiles;
/**
 * Class represents Command for checking again non-valid profiles (they got unbanned etc..)
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class CheckCommand extends Command {
    public CheckCommand() {
        super();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute() {
        System.out.println("Validating non-valid profiles");
        InstagramSearchUsernameResult userResult = null;
        final DaoUserProfiles uDao = DaoUserProfiles.getDao();
        ArrayList<Profile> users;
        try {
            users = (ArrayList<Profile>)uDao.getNonValids();
        }
        catch (SQLException e1) {
            System.err.println("Could not load profiles (" + e1.getMessage() + ")");
            return;
        }
        for (final Profile user : users) {
            try {
                userResult = (InstagramSearchUsernameResult)App.instagram().sendRequest((InstagramRequest)new InstagramSearchUsernameRequest(user.getName()));
            }
            catch (Exception e2) {
            }
            System.out.println(userResult.getStatus());
            if (!"fail".equals(userResult.getStatus())) {
                uDao.validAgain(user);
                System.out.println("Valid again: " + user.getName());
            }else {
            	System.out.println("Still non-valid: " + user.getName());
            }
        }
        System.out.println("Finished");
    }
    
    
    public String executePhrase() {
        return "check-again";
    }
    
    public String description() {
        return "Will check all non-valid profiles, if they are valid again (unbanned profiles etc.).";
    }
}
