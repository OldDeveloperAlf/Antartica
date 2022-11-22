package wtf.myles.hcfcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.staff.StaffMode;

/**
 * Created by Myles on 25/06/2015.
 */
public class Teleport extends CommandBase {

    public Teleport() {
        super("teleport", "Teleport to a player", Category.MOD, true);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 1) {
            if(Bukkit.getPlayer(args[0]) != null) {
                Player target = Bukkit.getPlayer(args[0]);
                p.teleport(target);
                p.sendMessage("You have teleported to " + ChatColor.GOLD + target.getName() + ChatColor.RESET + ".");
                return;
            } else {
                p.sendMessage(ChatColor.RED + "That player is not online.");
                return;
            }
        } else if(args.length == 2) {
            if(Bukkit.getPlayer(args[0]) != null && Bukkit.getPlayer(args[1]) != null) {
                Player target1 = Bukkit.getPlayer(args[0]);
                Player target2 = Bukkit.getPlayer(args[1]);
                target1.teleport(target2);
                p.sendMessage("You have teleported " + ChatColor.GOLD + target1.getName() + ChatColor.RESET + " to " + ChatColor.GOLD + target2.getName() + ChatColor.RESET + ".");
                return;
            } else {
                p.sendMessage(ChatColor.RED + "That player is not online.");
                return;
            }
        } else {
            p.sendMessage(ChatColor.RED + "Usage: /" + label + " <playerName> [playerName]");
            return;
        }
    }
}
