package com.exorath.plugin.game.cakewars.shop;

import com.exorath.exomenus.MenuItem;
import com.exorath.plugin.game.cakewars.Main;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;


/**
 * Created by toonsev on 3/18/2017.
 */
public class BuyableItem extends MenuItem {
    private int slot;
    public BuyableItem(String title, Material material, int amount, int slot) {
        super(title, new ItemStack(material, amount), null);
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public static BuyableItem getItem(ConfigurationSection section){
        if(!section.contains("title"))
            Main.terminate("BuyableItem section does not contain 'title' field");
        if(!section.contains("material"))
            Main.terminate("BuyableItem section does not contain 'material' field");
        if(!section.contains("amount"))
            Main.terminate("BuyableItem section does not contain 'amount' field");
        if(!section.contains("slot"))
            Main.terminate("BuyableItem section does not contain 'slot' field");
        return new BuyableItem(section.getString("title"),
                Material.valueOf(section.getString("material")),
                section.getInt("amount"),
                section.getInt("slot"));
    }
}
