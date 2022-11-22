package wtf.myles.hcfcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;

/**
 * Created by Myles on 02/07/2015.
 */
public class Weather extends CommandBase {

    public Weather() {
        super("weather", "Set the weather", Category.ADMIN, true);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 0 || args.length > 1) {
            p.sendMessage(ChatColor.RED + "Usage: /" + label + " [sun/storm]");
            return;
        }

        switch(args[0]) {
            case "sun":
            case "sunny":
                p.getWorld().setStorm(false);
                p.sendMessage(ChatColor.GREEN + "Weather is now sunny.");
                return;
            case "storm":
            case "rain":
                p.getWorld().setStorm(true);
                p.sendMessage(ChatColor.GREEN + "Weather is now stormy");
                return;
            default:
                p.sendMessage(ChatColor.RED + "Weather can only be set to sun or rain");
                return;
        }
    }
}
