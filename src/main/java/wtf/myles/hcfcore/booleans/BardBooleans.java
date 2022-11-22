package wtf.myles.hcfcore.booleans;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Relation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Created by Myles on 21/06/2015.
 */
public class BardBooleans {

    public static boolean enemyWithinRange(Player p, double x, double y, double z) {
        boolean inRange = false;
        for(Entity withinRange : p.getNearbyEntities(x, y, z)) {
            if(withinRange instanceof Player) {
                Player player = (Player) withinRange;
                FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
                FPlayer toBeChecked = FPlayers.getInstance().getByPlayer(p);
                if(fPlayer.getRelationTo(toBeChecked) != Relation.ALLY || fPlayer.getRelationTo(toBeChecked) != Relation.MEMBER || fPlayer.getRelationTo(toBeChecked) == Relation.NEUTRAL) {
                    inRange = true;
                }
            }
            inRange = false;
        }
        return inRange;
    }

    public static boolean teamWithinRange(Player p, double x, double y, double z) {
        boolean inRange = false;
        for(Entity withinRange : p.getNearbyEntities(x, y, z)) {
            if (withinRange instanceof Player) {
                Player player = (Player) withinRange;
                FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
                FPlayer toBeChecked = FPlayers.getInstance().getByPlayer(p);
                if(fPlayer.getRelationTo(toBeChecked) != Relation.ENEMY) {
                    inRange = true;
                }
            }
            inRange = false;
        }
        return inRange;
    }


}
