package wtf.myles.hcfcore.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Myles on 03/07/2015.
 */
public class ConversationManager {

    private static ConversationManager instance;

    public static ConversationManager getInstance() {
        return instance;
    }

    public ConversationManager() {
        instance = this;
    }

    private HashMap<String, String> conversations = new HashMap<>();

    public void setConversations(Player p, Player target, String message) {
        this.conversations.put(p.getName(), target.getName());
        this.conversations.put(target.getName(), p.getName());

        p.sendMessage(ChatColor.LIGHT_PURPLE + "(me -> " + target.getDisplayName() + ChatColor.LIGHT_PURPLE + ") " + message);
        target.sendMessage(ChatColor.LIGHT_PURPLE + "(" + p.getDisplayName() + ChatColor.LIGHT_PURPLE + " -> me) " + message);
        target.playSound(target.getLocation(), Sound.ORB_PICKUP, 10F, 10F);
    }

    public void reply(Player p, String message) {
        if(!this.conversations.containsKey(p.getName())) {
            p.sendMessage(ChatColor.RED + "You are not in a conversation with anyone.");
            return;
        }
        if(Bukkit.getPlayer(this.conversations.get(p.getName())) != null) {
            Player target = Bukkit.getPlayer(this.conversations.get(p.getName()));
            setConversations(p, target, message);
            target.playSound(target.getLocation(), Sound.ORB_PICKUP, 10F, 10F);
            return;
        } else {
            p.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }
    }

    public boolean isConversing(Player player)
    {
        return this.conversations.containsKey(player.getName());
    }

    public HashMap<String, String> getConversations()
    {
        return this.conversations;
    }

}
