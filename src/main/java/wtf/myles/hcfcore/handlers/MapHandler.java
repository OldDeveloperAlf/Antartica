package wtf.myles.hcfcore.handlers;


import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParser;
import org.bukkit.enchantments.Enchantment;
import wtf.myles.hcfcore.Main;
import wtf.myles.hcfcore.listeners.BorderListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Myles on 18/06/2015.
 */
public class MapHandler {

    private Map<Enchantment, Integer> maxEnchantments;

    public MapHandler() {
        try {
            maxEnchantments = new HashMap<>();
            File mapInfo = new File(Main.getInstance().getDataFolder() + File.separator + "mapInfo.json");
                if(!mapInfo.exists()) {
                    final BasicDBObject dbObject = new BasicDBObject();
                    final BasicDBObject enchants = new BasicDBObject();
                    dbObject.put("borderSize", 3000);
                    enchants.put("PROTECTION_ENVIRONMENTAL", Enchantment.PROTECTION_ENVIRONMENTAL.getMaxLevel());
                    enchants.put("ARROW_DAMAGE", Enchantment.ARROW_DAMAGE.getMaxLevel());
                    enchants.put("DAMAGE_ALL", Enchantment.DAMAGE_ALL.getMaxLevel());
                    enchants.put("ARROW_FIRE", Enchantment.ARROW_FIRE.getMaxLevel());
                    enchants.put("PROTECTION_FIRE", Enchantment.PROTECTION_FIRE.getMaxLevel());
                    enchants.put("THORNS", Enchantment.THORNS.getMaxLevel());
                    enchants.put("KNOCKBACK", Enchantment.KNOCKBACK.getMaxLevel());
                    enchants.put("FIRE_ASPECT", Enchantment.FIRE_ASPECT.getMaxLevel());
                    enchants.put("ARROW_KNOCKBACK", Enchantment.ARROW_KNOCKBACK.getMaxLevel());
                    enchants.put("LOOT_BONUS_MOBS", Enchantment.LOOT_BONUS_MOBS.getMaxLevel());
                    enchants.put("LOOT_BONUS_BLOCKS", Enchantment.LOOT_BONUS_BLOCKS.getMaxLevel());
                    enchants.put("ARROW_INFINITE", Enchantment.ARROW_INFINITE.getMaxLevel());
                    dbObject.put("enchants", enchants);
                    FileUtils.write(mapInfo, (CharSequence) new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(dbObject.toString())));
                }
            final BasicDBObject dbo = (BasicDBObject) JSON.parse(FileUtils.readFileToString(mapInfo));

            if(dbo != null) {
                final BasicDBObject enchants = (BasicDBObject)dbo.get("enchants");
                BorderListener.BORDER_SIZE = dbo.getInt("borderSize");
                for(final Map.Entry<String, Object> enchant : enchants.entrySet()) {
                    this.maxEnchantments.put(Enchantment.getByName(enchant.getKey()), (Integer) enchant.getValue());
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Enchantment, Integer> getMaxEnchantments() {
        return this.maxEnchantments;
    }
}
