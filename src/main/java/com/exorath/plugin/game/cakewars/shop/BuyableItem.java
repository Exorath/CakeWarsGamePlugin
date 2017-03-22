package com.exorath.plugin.game.cakewars.shop;

import com.exorath.exomenus.MenuItem;
import com.exorath.plugin.game.cakewars.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;


/**
 * Created by toonsev on 3/18/2017.
 */
public class BuyableItem extends MenuItem {
    private int slot;

    public BuyableItem(String name, Material material, int amount, int slot) {
        this(name, material, amount, slot, null);
    }

    public BuyableItem(String name, Material material, int amount, int slot, String... lore) {
        super(name, new ItemStack(material, amount), lore == null ? new String[0] : lore);
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public static BuyableItem getItem(Map<String, Object> section) {
        if (!section.containsKey("name"))
            Main.terminate("BuyableItem section does not contain 'name' field");
        if (!section.containsKey("material"))
            Main.terminate("BuyableItem section does not contain 'material' field");
        if (!section.containsKey("amount"))
            Main.terminate("BuyableItem section does not contain 'amount' field");
        if (!section.containsKey("slot"))
            Main.terminate("BuyableItem section does not contain 'slot' field");
        BuyableItem item = new BuyableItem((String) section.get("name"),
                Material.valueOf((String) section.get("material")),
                (Integer) section.get("amount"),
                (Integer) section.get("slot"));
        item.getClickObservable().subscribe(event -> buy((Player) event.getWhoClicked(), item));
        return item;
    }

    private static void buy(Player player, BuyableItem item) {
        System.out.println("TODO: Buy " + item.getTitle() + " for " + player.getName());
    }
}
