package com.exorath.plugin.game.cakewars.shop;

import com.exorath.exomenus.MenuItem;
import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.game.cakewars.Main;
import com.exorath.plugin.game.cakewars.spawners.SpawnerType;
import com.exorath.plugin.game.cakewars.spawners.SpawnersManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by toonsev on 3/18/2017.
 */
public class BuyableItem extends MenuItem {
    private int slot;
    private Map<SpawnerType, Integer> costsPerSpawnerType;

    public BuyableItem(String name, Material material, int amount, Map<SpawnerType, Integer> costsPerSpawnerType, int slot) {
        this(name, material, amount, slot, costsPerSpawnerType, null);
    }

    public BuyableItem(String name, Material material, int amount, int slot, Map<SpawnerType, Integer> costsPerSpawnerType, String... lore) {
        super(name, new ItemStack(material, amount), getLore(costsPerSpawnerType, lore));
        this.slot = slot;
        this.costsPerSpawnerType = costsPerSpawnerType;
    }

    private static String[] getLore(Map<SpawnerType, Integer> costsPerSpawnerType, String... baseLore) {
        List<String> lore = baseLore == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(baseLore));

        for (Map.Entry<SpawnerType, Integer> entry : costsPerSpawnerType.entrySet())
            lore.add("Cost: " + ChatColor.WHITE + entry.getValue() + " " + entry.getKey());
        return lore.toArray(new String[lore.size()]);
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
        Map<SpawnerType, Integer> costs = loadCost((Map<String, Object>) section.get("cost"), BaseGameAPI.getInstance().getManager(SpawnersManager.class));
        BuyableItem item = new BuyableItem((String) section.get("name"),
                Material.valueOf((String) section.get("material")),
                (Integer) section.get("amount"), costs,
                (Integer) section.get("slot"));
        item.getClickObservable().subscribe(event -> buy((Player) event.getWhoClicked(), item));
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
        System.out.println("TODO: Buy " + item.getTitle() + " for " + player.getName());
    }
}
