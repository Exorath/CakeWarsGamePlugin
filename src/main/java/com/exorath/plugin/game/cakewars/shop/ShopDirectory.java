package com.exorath.plugin.game.cakewars.shop;

import com.exorath.exomenus.InventoryMenu;
import com.exorath.exomenus.MenuItem;
import com.exorath.exomenus.Size;
import com.exorath.plugin.game.cakewars.Main;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by toonsev on 3/18/2017.
 */
public class ShopDirectory extends MenuItem {
    private InventoryMenu menu;
    private int slot;

    public ShopDirectory(ShopMenu shopMenu, String name, Material material, int slot, Collection<BuyableItem> buyableItems, String... lore) {
        super(name, new ItemStack(material, 1), lore);
        this.slot = slot;
        this.menu = new InventoryMenu(name, Size.ONE_LINE, buyableItems.toArray(new BuyableItem[buyableItems.size()]), shopMenu.getMenu());
        buyableItems.forEach(item -> menu.setItem(item.getSlot(), item));
    }

    public int getSlot() {
        return slot;
    }

    public InventoryMenu getMenu() {
        return menu;
    }

    public static ShopDirectory load(ShopMenu menu, ConfigurationSection directorySection){
        if(!directorySection.contains("slot"))
            Main.terminate("Shop directory does not contain 'slot' field");
        if(!directorySection.contains("name"))
            Main.terminate("Shop directory does not contain 'name' field");
        if(!directorySection.contains("material"))
            Main.terminate("Shop directory does not contain 'material' field");
        if(!directorySection.contains("items"))
            Main.terminate("Shop directory does not contain 'items' field");
        if(!directorySection.contains("lore"))
            Main.terminate("Shop directory does not contain 'lore' field");
        List<String>  lore = directorySection.getStringList("lore");
        List<BuyableItem> items = ((List<ConfigurationSection>) directorySection.getList("items", new ArrayList<>()))
                .stream().map(itemSection -> BuyableItem.getItem(itemSection))
                .collect(Collectors.toList());
        return new ShopDirectory(menu,
                directorySection.getString("name"),
                Material.valueOf(directorySection.getString("material")),
                directorySection.getInt("slot"),
                items,
                lore.toArray(new String[lore.size()]));

    }
}
