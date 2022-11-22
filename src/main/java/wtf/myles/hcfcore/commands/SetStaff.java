package wtf.myles.hcfcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.managers.ProfileManager;
import wtf.myles.hcfcore.objects.Profile;

/**
 * Created by Myles on 14/07/2015.
 */
public class SetStaff extends CommandBase {

    public SetStaff() {
        super("setstaff", "Set a player to a staff member", Category.OP, false);
    }

    ProfileManager pm = Main.getInstance().getProfileManager();

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 0 || args.length > 1) {
            if(args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if(target != null) {
                    Profile prof = pm.getProfile(target.getUniqueId());
                    if (prof != null) {
                        prof.setStaff(true);
                        prof.setAlerts(true);
                        sender.sendMessage(ChatColor.GREEN + target.getName() + " is now a staff member.");
                        return;
                    } else {
                        sender.sendMessage(ChatColor.RED + "That player does not have a profile.");
                        return;
                    }
                }
                sender.sendMessage(ChatColor.RED + "That player is not online.");
                return;
            }
        }
        sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <playerName>");
        return;
    }
}
