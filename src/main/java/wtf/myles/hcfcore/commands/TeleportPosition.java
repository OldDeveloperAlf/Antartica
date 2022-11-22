package wtf.myles.hcfcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.utils.LocationUtils;
import wtf.myles.hcfcore.utils.NumberUtils;

/**
 * Created by Myles on 25/06/2015.
 */
public class TeleportPosition extends CommandBase {

    public TeleportPosition() {
        super("teleportposition", "Teleport to coords", Category.ADMIN, true);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 3) {
            if(NumberUtils.isNumber(args[0]) && NumberUtils.isNumber(args[1]) && NumberUtils.isNumber(args[2])) {
                Integer x = Integer.parseInt(args[0]);
                Integer y = Integer.parseInt(args[1]);
                Integer z = Integer.parseInt(args[2]);

                Location location = new Location(p.getWorld(), x, y, z);
                String serializedLocation = LocationUtils.serializeLocation(location);
                p.teleport(location);

                p.sendMessage(ChatColor.GREEN + "You have been teleported to " + LocationUtils.serializeLocation(location) + ".");
                return;
            } else {
                p.sendMessage(ChatColor.RED + "You only need to input numbers for this command.");
                return;
            }
        } else {
            p.sendMessage(ChatColor.RED + "Usage: /" + label + " <x> <y> <z>");
            return;
        }
    }
}
