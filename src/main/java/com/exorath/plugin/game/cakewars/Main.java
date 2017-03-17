package com.exorath.plugin.game.cakewars;

import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.basegame.maps.MapsManager;
import com.exorath.plugin.game.cakewars.spawners.SpawnersManager;
import com.exorath.plugin.game.cakewars.team.CWTeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by toonsev on 3/15/2017.
 */
public class Main extends JavaPlugin{
    private static Main instance;
    private BaseGameAPI baseGameAPI;
    @Override
    public void onEnable() {
        Main.instance = this;
        this.baseGameAPI = BaseGameAPI.getInstance();
        System.out.println("config: ");
        for(String key : baseGameAPI.getMapsManager().getGameMap().getConfiguration().getKeys(true))
            System.out.println(key);
        System.out.println("--------");
        ConfigurationSection teamsSection = baseGameAPI.getMapsManager().getGameMap().getConfiguration().getConfigurationSection("teams");
        baseGameAPI.addManager(new CWTeamManager(baseGameAPI.getTeamAPI(), teamsSection));
        ConfigurationSection spawnersSection = baseGameAPI.getMapsManager().getGameMap().getConfiguration().getConfigurationSection("spawners");
        baseGameAPI.addManager(new SpawnersManager(spawnersSection));
    }

    public static Main getInstance(){
        return instance;
    }
    public static void terminate() {
        System.out.println("1v1Plugin is terminating...");
        Bukkit.shutdown();
        System.out.println("Termination failed, force exiting system...");
        System.exit(1);
    }

    public static Location getLocation(ConfigurationSection configSection){
        if(!configSection.contains("world")){
            System.out.println("Tried to deserialize a location config section but it did not contain a world field");
            Main.terminate();
        }
        return getLocation(Bukkit.createWorld(new WorldCreator(configSection.getString("world"))), configSection);
    }

    public static Location getLocation(World world, ConfigurationSection configSection){
        if(!configSection.contains("x") || !configSection.contains("y") || !configSection.contains("z")){
            System.out.println("Tried to deserialize a location config section but it did not contain an x, y or z");
            Main.terminate();
        }
        return new Location(world, configSection.getDouble("x"), configSection.getDouble("y"), configSection.getDouble("z"));
    }

    public MapsManager getMapsManager(){
        return baseGameAPI.getMapsManager();
    }
}
