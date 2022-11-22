package wtf.myles.hcfcore.commands;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.commandusage.Category;
import wtf.myles.hcfcore.commandusage.CommandBase;
import wtf.myles.hcfcore.managers.ProfileManager;
import wtf.myles.hcfcore.objects.Profile;
import wtf.myles.hcfcore.utils.Lists;

/**
 * Created by Myles on 30/06/2015.
 */
public class StaffChat extends CommandBase {

    public StaffChat() {
        super("staffchat", "Toggle Staff Chat", Category.MOD, true);
    }
    ProfileManager pm = Main.getInstance().getProfileManager();
    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        Profile profile = pm.getProfile(p.getUniqueId());
        if(args.length == 0) {
            profile.setStaffChat(!profile.isStaffChat());
            p.sendMessage(ChatColor.YELLOW + (profile.isStaffChat() ? "You are now in the staff chat." : "You are no longer in the staff chat."));
            return;
        } else {
            String message = StringUtils.join(args, " ", 0, args.length);
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online.hasPermission("command.mod")) {
                    online.sendMessage(ChatColor.RED + p.getName() + ": " + message);
                }
            }
        }
    }
}
