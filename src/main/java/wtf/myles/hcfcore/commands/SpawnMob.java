package wtf.myles.hcfcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.utils.Mob;
import wtf.myles.hcfcore.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myles on 30/06/2015.
 */
public class SpawnMob extends CommandBase {

    public SpawnMob() {
        super("spawnmob", "Spawn a mob", Category.ADMIN, true);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 0) {
            p.sendMessage(ChatColor.RED + "Usage: /" + label + " <mob>[,mount] <amount>");
            return;
        }
        String mobs = args[0];
        String amountStr = args.length == 1 ? "1" : args[1];
        if(NumberUtils.isNumber(amountStr)) {
            int amount = Integer.parseInt(amountStr);
            String[] mobArray = mobs.split(",");
            List<EntityType> entityTypes = new ArrayList<>();
            boolean cancelled = false;
            List<String> invalidMobs = new ArrayList<>();
            for(String str : mobArray) {
                if(isLivingEntity(str)) {
                    entityTypes.add(Mob.valueOf(str.toUpperCase()).getType());
                } else {
                    invalidMobs.add(str);
                    cancelled = true;
                }
            }
            if(!cancelled) {
                Entity lastEntity;
                for(int i = 1; i <= amount; i++) {
                    lastEntity = null;
                    for(EntityType entityType : entityTypes) {
                        Entity newEntity = p.getWorld().spawnEntity(p.getLocation(), entityType);
                        if(lastEntity != null) {
                            lastEntity.setPassenger(newEntity);
                        }
                        lastEntity = newEntity;
                    }
                }
                p.sendMessage(ChatColor.RED + "Spawned " + amount + " set(s) of mobs.");
            } else {
                p.sendMessage(ChatColor.RED + "You may not spawn these type of mobs: " + invalidMobs.toString());
                return;
            }
        } else {
            p.sendMessage(ChatColor.RED + "Please input numbers");
            return;
        }
    }

    private boolean isLivingEntity(String str) {
        for(Mob mob : Mob.values()) {
            if(mob.toString().equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}
