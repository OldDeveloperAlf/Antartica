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
 * Created by Myles on 08/07/2015.
 */
public class CommandSpy extends CommandBase {

    ProfileManager pm = Main.getInstance().getProfileManager();

    public CommandSpy() {
        super("commandspy", "Toggle command spy", Category.MOD, true);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        Profile profile = pm.getProfile(p.getUniqueId());
        if(args.length == 0) {
            profile.setCommandSpy(!profile.isCommandSpy());
            p.sendMessage(ChatColor.YELLOW + (profile.isCommandSpy() ? "You are now in commandspy mode." : "You are no longer in commandspy mode."));
            return;
        }
        p.sendMessage(ChatColor.RED + "Usage: /" + label);
        return;
    }
}
