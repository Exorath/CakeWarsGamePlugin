package com.exorath.plugin.game.cakewars;

import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.basegame.clickableEntities.ClickableEntitiesManager;
import com.exorath.plugin.basegame.flavor.FlavorManager;
import com.exorath.plugin.basegame.maps.MapsManager;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.game.cakewars.config.ConfigProvider;
import com.exorath.plugin.game.cakewars.config.FileConfigProvider;
import com.exorath.plugin.game.cakewars.kits.KitsManager;
import com.exorath.plugin.game.cakewars.shop.ShopManager;
import com.exorath.plugin.game.cakewars.spawners.SpawnersManager;
import com.exorath.plugin.game.cakewars.startTeleport.StartTeleportManager;
import com.exorath.plugin.game.cakewars.team.CWTeamManager;
import com.exorath.service.kit.api.KitServiceAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by toonsev on 3/15/2017.
 */
public class Main extends JavaPlugin{
    public static final String CRUMBS_CURRENCY = "Crumbs";
    private static Main instance;
    private BaseGameAPI baseGameAPI;
    private ConfigProvider configProvider;
    @Override
    public void onEnable() {
        Main.instance = this;
        this.baseGameAPI = BaseGameAPI.getInstance();
        try {
            this.configProvider = new FileConfigProvider(new File(getDataFolder(), "config.json"));
        } catch (FileNotFoundException e) {
            Main.terminate(e.getMessage());
        }
        String flavor = baseGameAPI.getManager(FlavorManager.class).getFlavor();
        ConfigurationSection flavorSection = getConfig().getConfigurationSection("flavors." + flavor);
        ConfigurationSection teamsSection = baseGameAPI.getMapsManager().getGameMap().getConfiguration().getConfigurationSection("teams");
        baseGameAPI.addManager(new CWTeamManager(baseGameAPI.getTeamAPI(), teamsSection));
        ConfigurationSection spawnersSection = baseGameAPI.getMapsManager().getGameMap().getConfiguration().getConfigurationSection("spawners");
        baseGameAPI.addManager(new SpawnersManager(spawnersSection));
        baseGameAPI.addManager(new StartTeleportManager(baseGameAPI.getTeamAPI()));
        baseGameAPI.addManager(new ShopManager(baseGameAPI.getManager(ClickableEntitiesManager.class), baseGameAPI.getTeamAPI().getTeams(), flavorSection.getConfigurationSection("shop")));//depends on spawner
        baseGameAPI.addManager(new KitsManager(new KitServiceAPI(getKitServiceAddress()), configProvider.getKitPackageJson()));
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
    private String getKitServiceAddress() {
        String address = System.getenv("KIT_SERVICE_ADDRESS");
        if (address == null)
            Main.terminate("No KIT_SERVICE_ADDRESS env found.");
        return address;
    }
    public MapsManager getMapsManager(){
        return baseGameAPI.getMapsManager();
    }
}
