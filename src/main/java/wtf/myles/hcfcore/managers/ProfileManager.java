package wtf.myles.hcfcore.managers;

import wtf.myles.hcfcore.objects.Profile;

import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Myles on 07/07/2015.
 */
public class ProfileManager {

    private HashSet<Profile> profiles = new HashSet<Profile>();

    public HashSet<Profile> getProfiles() {
        return profiles;
    }

    public Profile getProfile(UUID uuid) {
        for(Profile profile : getProfiles()) {
            if(profile.getUuid() == uuid) {
                return profile;
            }
        }
        return null;
    }


    public boolean hasProfile(UUID id) {
        for (Profile prof : getProfiles()) {
            if (prof.getUuid().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public void createProfile(UUID uuid) {
        if(getProfile(uuid) == null) {
            Profile prof = new Profile(uuid);
            getProfiles().add(prof);
        }
    }
}
