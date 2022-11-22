package wtf.myles.hcfcore.managers;

import com.comphenix.protocol.PacketType;

import java.util.HashMap;

/**
 * Created by Myles on 29/06/2015.
 */
public class ServerManager {

    private boolean chatMuted = false;
    private boolean locked = false;
    private int chatSlow = 0;
    private HashMap<String, Long> slowCooldowns = new HashMap<>();
    private static ServerManager serverManager;

    public ServerManager() {
        serverManager = this;
    }

    public static ServerManager getInstance() {
        return serverManager;
    }

    public boolean isChatMuted() {
        return chatMuted;
    }

    public void setChatMuted(boolean chatMuted) {
        this.chatMuted = chatMuted;
    }

    public int getChatSlow() {
        return chatSlow;
    }

    public void setChatSlow(int chatSlow) {
        this.chatSlow = chatSlow;
    }

    public HashMap<String, Long> getSlowCooldowns() {
        return slowCooldowns;
    }

    public void setSlowCooldowns(HashMap<String, Long> slowCooldowns) {
        this.slowCooldowns = slowCooldowns;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
