package wtf.myles.hcfcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;

/**
 * Created by Myles on 25/06/2015.
 */
public class TeleportHere extends CommandBase {

    public TeleportHere() {
        super("teleporthere", "Teleport a player to you", Category.MOD, true);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 1) {
            if(Bukkit.getPlayer(args[0]) != null) {
                Player target = Bukkit.getPlayer(args[0]);
                target.teleport(p);
                p.sendMessage("You have teleported " + ChatColor.GOLD + target.getName() + ChatColor.RESET + " to you.");
                return;
            } else {
                p.sendMessage(ChatColor.RED + "That player is not online");
                return;
            }
        } else {
            p.sendMessage(ChatColor.RED + "Usage: /" + label + " <playerName>");
            return;
        }
    }
}
