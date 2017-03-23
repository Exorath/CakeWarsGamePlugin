package com.exorath.plugin.game.cakewars.spawners;

import com.exorath.plugin.basegame.lib.LocationSerialization;
import com.exorath.plugin.basegame.manager.Manager;
import com.exorath.plugin.game.cakewars.Main;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by toonsev on 3/17/2017.
 */
public class SpawnersManager implements Manager {
    private ConfigurationSection spawnersSection;

    private HashMap<String, SpawnerType> spawnerTypes = new HashMap<>();
    private Set<Spawner> spawners = new HashSet<>();

    public SpawnersManager(ConfigurationSection spawnersSection) {
        if(spawnersSection == null)
            Main.terminate("No spawnerssection found in map config.");
        this.spawnersSection = spawnersSection;
        for(String key : spawnersSection.getKeys(true))
            System.out.println(key);
        loadSpawnerTypes(spawnersSection.getConfigurationSection("spawnerTypes"));
        loadSpawners(spawnersSection.getConfigurationSection("spawners"));
    }

    private void loadSpawnerTypes(ConfigurationSection spawnerTypesSection) {
        if(spawnerTypesSection == null)
            Main.terminate("No spawnerTypes section found");
        for (String key : spawnerTypesSection.getKeys(false)) {
            ConfigurationSection typeSection = spawnerTypesSection.getConfigurationSection(key);
            if (!typeSection.contains("material"))
                Main.terminate("No type material in a spawnerType config");
            if (!typeSection.contains("interval"))
                Main.terminate("No interval field in a spawnerType config");
            SpawnerType type = new SpawnerType(Material.valueOf(typeSection.getString("material")), typeSection.getLong("interval"));
            spawnerTypes.put(key, type);
        }
    }

    public HashMap<String, SpawnerType> getSpawnerTypes() {
        return spawnerTypes;
    }

    private void loadSpawners(ConfigurationSection spawnersSection) {
        if(spawnersSection == null){
            System.out.println("No spawners section found");
            Main.terminate();
        }

        World world = Main.getInstance().getMapsManager().getGameMap().getWorld();
        for (String key : spawnersSection.getKeys(false)) {
            ConfigurationSection spawnerSection = spawnersSection.getConfigurationSection(key);
            if (!spawnerSection.contains("type"))
                Main.terminate("No type field in a spawner config");
            if (!spawnerSection.contains("location"))
                Main.terminate("No location field in a spawner config");

            SpawnerType spawnerType = spawnerTypes.get(spawnerSection.getString("type"));
            if (spawnerType == null)
                Main.terminate("a spawner type was not found in spawner config");
            Spawner spawner = new Spawner(LocationSerialization.getLocation(world, spawnerSection.getConfigurationSection("location")), spawnerType);
            spawner.start();
            spawners.add(spawner);
        }
    }

}
