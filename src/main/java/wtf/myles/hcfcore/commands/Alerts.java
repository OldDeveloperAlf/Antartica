package wtf.myles.hcfcore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.managers.ProfileManager;
import wtf.myles.hcfcore.objects.Profile;

/**
 * Created by Myles on 14/07/2015.
 */
public class Alerts extends CommandBase {

    public Alerts() {
        super("alerts", "Toggle alerts", Category.MOD, true);
    }

    ProfileManager pm = Main.getInstance().getProfileManager();

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(args.length == 0) {
            Profile profile = pm.getProfile(p.getUniqueId());
            profile.setAlerts(!profile.isAlerts());
            p.sendMessage(ChatColor.YELLOW + (profile.isAlerts() ? "You will now see alerts." : "You will no longer see alerts."));
            return;
        }
        p.sendMessage(ChatColor.RED + "Usage: /" + label);
        return;
    }
}
