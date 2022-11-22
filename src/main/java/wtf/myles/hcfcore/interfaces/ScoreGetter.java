package wtf.myles.hcfcore.interfaces;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import wtf.myles.hcfcore.handlers.SpawnTagHandler;

/**
 * Created by Myles on 22/06/2015.
 */
public interface ScoreGetter {

    public static final int NO_SCORE = -1;

    public static final ScoreGetter SPAWN_TAG = new ScoreGetter() {
        @Override
        public String getTitle(Player p0) {
            return ChatColor.GREEN.toString() + "Spawn Tag";
        }

        @Override
        public int getSeconds(Player p0) {
            long time;
            if(SpawnTagHandler.isTagged(p0)) {
                time = SpawnTagHandler.getTag(p0);
                if(time >= 0L) {
                    return (int)time / 1000;
                }
            }
            return NO_SCORE;
        }
    };

    public static final ScoreGetter PVPTIMER = new ScoreGetter() {
        @Override
        public String getTitle(Player p0) {
            return ChatColor.RED.toString() + "PvPTimer";
        }

        @Override
        public int getSeconds(Player p0) {
            long time;
            if(PvPTimerHandler.hasPvPTimer(p0)) {
                time = PvPTimerHandler.getPvPTimer(p0);
                if(time >= 0L) {
                    return (int)time / 1000;
                }
            }
            return NO_SCORE;
        }
    };

    public static final ScoreGetter ENDER_PEARL = new ScoreGetter() {
        @Override
        public String getTitle(Player p0) {
           return ChatColor.YELLOW.toString() + "Enderpearl";
        }

        @Override
        public int getSeconds(Player p0) {
            long time;
            return NO_SCORE;
        }
    };

    public static final ScoreGetter[] SCORES = { ScoreGetter.SPAWN_TAG, ScoreGetter.ENDER_PEARL, ScoreGetter.PVPTIMER };

    String getTitle(Player p0);
    int getSeconds(Player p0);
}
