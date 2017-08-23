/*
 * Copyright 2017 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.plugin.game.cakewars.shop;

import com.exorath.exomenus.InventoryMenu;
import com.exorath.exomenus.MenuItem;
import com.exorath.exomenus.Size;
import com.exorath.plugin.game.cakewars.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

    public static ShopDirectory load(ShopMenu menu, Map<String, Object> directorySection) {
        if (!directorySection.containsKey("slot"))
            Main.terminate("Shop directory does not contain 'slot' field");
        if (!directorySection.containsKey("name"))
            Main.terminate("Shop directory does not contain 'name' field");
        if (!directorySection.containsKey("material"))
            Main.terminate("Shop directory does not contain 'material' field");
        if (!directorySection.containsKey("items"))
            Main.terminate("Shop directory does not contain 'items' field");
        if (!directorySection.containsKey("lore"))
            Main.terminate("Shop directory does not contain 'lore' field");
        List<String> lore = (ArrayList<String>) directorySection.getOrDefault("lore", new ArrayList<>());

        List<BuyableItem> items = ((ArrayList<Map<String, Object>>) directorySection.getOrDefault("items", new ArrayList<>()))
                .stream().map(itemMap -> BuyableItem.getItem(itemMap))
                .collect(Collectors.toList());


        ShopDirectory directory = new ShopDirectory(menu,
                (String) directorySection.get("name"),
                Material.valueOf((String) directorySection.get("material")),
                (Integer) directorySection.get("slot"),
                items,
                lore.toArray(new String[lore.size()]));

        System.out.println("Arrived to subscribe");

        directory.getClickObservable().subscribe(event -> {
            System.out.println("In clickable loop");
            event.setCancelled(true);
            directory.getMenu().open((Player) event.getWhoClicked());
        });
        return directory;

    }
}
