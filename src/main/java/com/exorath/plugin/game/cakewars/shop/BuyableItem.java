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

import com.exorath.exomenus.MenuItem;
import com.exorath.plugin.base.ExoBaseAPI;
import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.game.cakewars.Main;
import com.exorath.plugin.game.cakewars.spawners.SpawnerType;
import com.exorath.plugin.game.cakewars.spawners.SpawnersManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by toonsev on 3/18/2017.
 */
public class BuyableItem extends MenuItem {
    private int slot;
    private String name;
    private Material material;
    private String[] lore;
    private int amount;
    private Map<SpawnerType, Integer> costsPerSpawnerType;

    public BuyableItem(String name, Material material, int amount, Map<SpawnerType, Integer> costsPerSpawnerType, int slot) {
        this(name, material, amount, slot, costsPerSpawnerType, null);
    }

    public BuyableItem(String name, Material material, int amount, int slot, Map<SpawnerType, Integer> costsPerSpawnerType, String... lore) {
        super(name, new ItemStack(material, amount), getLore(costsPerSpawnerType, lore));
        this.slot = slot;
        this.costsPerSpawnerType = costsPerSpawnerType;
        this.name = name;
        this.material = material;
        this.amount = amount;
        this.lore = lore;
    }

    private static String[] getLore(Map<SpawnerType, Integer> costsPerSpawnerType, String... baseLore) {
        List<String> lore = baseLore == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(baseLore));

        for (Map.Entry<SpawnerType, Integer> entry : costsPerSpawnerType.entrySet())
            lore.add("Cost: " + ChatColor.WHITE + entry.getValue() + " " + entry.getKey().getName());
        return lore.toArray(new String[lore.size()]);
    }

    public Map<SpawnerType, Integer> getCostsPerSpawnerType() {
        return costsPerSpawnerType;
    }

    public void addCost(SpawnerType spawnerType, Integer price) {
        costsPerSpawnerType.put(spawnerType, price);
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
        if (!section.containsKey("cost"))
            Main.terminate("BuyableItem section does not contain 'cost' field");
        Map<SpawnerType, Integer> costs = loadCost((Map<String, Object>) section.get("cost"), ExoBaseAPI.getInstance().getManager(SpawnersManager.class));
        BuyableItem item = new BuyableItem((String) section.get("name"),
                Material.valueOf((String) section.get("material")),
                (Integer) section.get("amount"), costs,
                (Integer) section.get("slot"));
        item.getClickObservable().subscribe(event -> {
            event.setCancelled(true);
            buy((Player) event.getWhoClicked(), item);
        });
        return item;
    }

    private static Map<SpawnerType, Integer> loadCost(Map<String, Object> costSection, SpawnersManager spawnersManager) {
        Map<SpawnerType, Integer> costBySpawnerTypes = costSection.entrySet().stream()
                .filter(entry -> spawnersManager.getSpawnerTypes().containsKey(entry.getKey()))
                .collect(Collectors.toMap(entry -> spawnersManager.getSpawnerTypes().get(entry.getKey()), entry -> (Integer) entry.getValue()));
        if (costBySpawnerTypes.size() != costSection.size())
            Main.terminate("A spawnType was not found in the item cost section");
        return costBySpawnerTypes;
    }

    private static void buy(Player player, BuyableItem item) {
        boolean enough = true;

        for (Map.Entry<SpawnerType, Integer> entry : item.getCostsPerSpawnerType().entrySet()) {
            if (!player.getInventory().contains(entry.getKey().getMaterial(), entry.getValue())) {
                enough = false;
                player.sendMessage(ChatColor.RED + "Not enough " + entry.getKey().getName());
            }
        }
        if (!enough)
            return;
        handlePurchase(player, item);
        player.sendMessage(ChatColor.GREEN + "Bought " + item.getTitle() + "!");
        player.getInventory().addItem(item.getActualItemStack(player)).forEach((integer, itemStack) -> player.getWorld().dropItem(player.getLocation(), itemStack));

    }

    private static void handlePurchase(Player player, BuyableItem item) {
        HashMap<Material, Integer> materials = new HashMap<>();
        item.getCostsPerSpawnerType().forEach((spawnerType, integer) -> {
            if (materials.containsKey(spawnerType.getMaterial()))
                materials.put(spawnerType.getMaterial(), materials.get(spawnerType.getMaterial()) + integer);
            else
                materials.put(spawnerType.getMaterial(), integer);
        });
        Set<Integer> toRemove = new HashSet<>();
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (itemStack != null && itemStack.getType() != null && materials.containsKey(itemStack.getType())) {
                int amount = materials.get(itemStack.getType());
                if (itemStack.getAmount() > amount) {
                    itemStack.setAmount(itemStack.getAmount() - amount);
                    break;
                } else if (itemStack.getAmount() == amount) {
                    toRemove.add(i);
                    break;
                } else {
                    materials.put(itemStack.getType(), amount - itemStack.getAmount());
                    toRemove.add(i);
                }
            }
        }
        toRemove.forEach(slot -> player.getInventory().clear(slot));
    }

    private ItemStack getActualItemStack(Player player) {
        ItemStack is = new ItemStack(material, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        if (lore != null)
            im.setLore(Arrays.asList(lore));
        return is;
    }
}
