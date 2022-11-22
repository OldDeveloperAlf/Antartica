package wtf.myles.hcfcore.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by VBS Aka TheS7W.
 * Package name: wtf.myles.hcfcore.utils
 * Date: 04/08/2015
 * Project name: Antarctica
 */
public class ItemBuilder
{
    private static final HashMap<String, PotionEffect> effects = new HashMap();
    private final ItemStack is;

    public ItemBuilder(Material mat)
    {
        this.is = new ItemStack(mat);
    }

    public ItemBuilder(ItemStack is)
    {
        this.is = is;
    }

    public ItemBuilder amount(int amount)
    {
        this.is.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name)
    {
        ItemMeta meta = this.is.getItemMeta();
        meta.setDisplayName(name);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String name)
    {
        ItemMeta meta = this.is.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList();
        }
        lore.add(name);
        meta.setLore(lore);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder durability(int durability)
    {
        this.is.setDurability((short)durability);
        return this;
    }

    public ItemBuilder data(int data)
    {
        this.is.setData(new MaterialData(this.is.getType(), (byte)data));
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level)
    {
        this.is.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment)
    {
        this.is.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder type(Material material)
    {
        this.is.setType(material);
        return this;
    }

    public ItemBuilder clearLore()
    {
        ItemMeta meta = this.is.getItemMeta();
        meta.setLore(new ArrayList());
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchantments()
    {
        for (Enchantment e : this.is.getEnchantments().keySet()) {
            this.is.removeEnchantment(e);
        }
        return this;
    }

    public ItemBuilder color(Color color)
    {
        if ((this.is.getType() == Material.LEATHER_BOOTS) || (this.is.getType() == Material.LEATHER_CHESTPLATE) || (this.is.getType() == Material.LEATHER_HELMET) || (this.is.getType() == Material.LEATHER_LEGGINGS))
        {
            LeatherArmorMeta meta = (LeatherArmorMeta)this.is.getItemMeta();
            meta.setColor(color);
            this.is.setItemMeta(meta);
            return this;
        }
        throw new IllegalArgumentException("color() only applicable for leather armor!");
    }

    public ItemBuilder effect(PotionEffectType type, int duration, int amplifier, boolean ambient)
    {
        effect(new PotionEffect(type, duration, amplifier, ambient));
        return this;
    }

    public ItemBuilder effect(PotionEffect effect)
    {
        String name = this.is.getItemMeta().getDisplayName();
        while (effects.containsKey(name)) {
            name = name + "#";
        }
        effects.put(name, effect);
        return this;
    }

    public ItemBuilder effect(PotionEffectType type, int duration, int amplifier)
    {
        effect(new PotionEffect(type, duration == -1 ? 1000000 : duration, amplifier));
        return this;
    }

    public ItemBuilder effect(PotionEffectType type, int duration)
    {
        effect(new PotionEffect(type, duration == -1 ? 1000000 : duration, 1));
        return this;
    }

    public ItemStack build()
    {
        return this.is;
    }
}
