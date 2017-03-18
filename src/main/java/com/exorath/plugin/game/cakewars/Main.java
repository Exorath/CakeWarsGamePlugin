package com.exorath.plugin.game.cakewars;

import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.basegame.clickableEntities.ClickableEntitiesManager;
import com.exorath.plugin.basegame.maps.MapsManager;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.game.cakewars.shop.ShopManager;
import com.exorath.plugin.game.cakewars.spawners.SpawnersManager;
import com.exorath.plugin.game.cakewars.startTeleport.StartTeleportManager;
import com.exorath.plugin.game.cakewars.team.CWTeamManager;
import org.bukkit.Bukkit;
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
        ConfigurationSection teamsSection = baseGameAPI.getMapsManager().getGameMap().getConfiguration().getConfigurationSection("teams");
        baseGameAPI.addManager(new CWTeamManager(baseGameAPI.getTeamAPI(), teamsSection));
        ConfigurationSection spawnersSection = baseGameAPI.getMapsManager().getGameMap().getConfiguration().getConfigurationSection("spawners");
        baseGameAPI.addManager(new SpawnersManager(spawnersSection));
        baseGameAPI.addManager(new StartTeleportManager(baseGameAPI.getTeamAPI()));
        baseGameAPI.addManager(new ShopManager(baseGameAPI.getManager(ClickableEntitiesManager.class), baseGameAPI.getTeamAPI().getTeams()));
        baseGameAPI.getStateManager().setState(State.WAITING_FOR_PLAYERS);
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
    public static void terminate(String message) {
        System.out.println(message);
        System.out.println("1v1Plugin is terminating...");
        Bukkit.shutdown();
        System.out.println("Termination failed, force exiting system...");
        System.exit(1);
    }
    public MapsManager getMapsManager(){
        return baseGameAPI.getMapsManager();
    }
}
