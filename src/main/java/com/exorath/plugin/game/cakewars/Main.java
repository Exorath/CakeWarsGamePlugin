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

package com.exorath.plugin.game.cakewars;

import com.exorath.exoteams.startRule.GlobalMinPlayersStartRule;
import com.exorath.plugin.base.ExoBaseAPI;
import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.basegame.clickableEntities.ClickableEntitiesManager;
import com.exorath.plugin.basegame.flavor.FlavorManager;
import com.exorath.plugin.basegame.maps.MapsManager;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.game.cakewars.cake.CakeManager;
import com.exorath.plugin.game.cakewars.config.ConfigProvider;
import com.exorath.plugin.game.cakewars.config.FileConfigProvider;
import com.exorath.plugin.game.cakewars.finish.FinishManager;
import com.exorath.plugin.game.cakewars.kits.KitsManager;
import com.exorath.plugin.game.cakewars.players.PlayerManager;
import com.exorath.plugin.game.cakewars.protection.GameProtectionManager;
import com.exorath.plugin.game.cakewars.rewards.RewardManager;
import com.exorath.plugin.game.cakewars.scoreboard.ScoreboardManager;
import com.exorath.plugin.game.cakewars.shop.ShopManager;
import com.exorath.plugin.game.cakewars.spawners.SpawnersManager;
import com.exorath.plugin.game.cakewars.startTeleport.StartTeleportManager;
import com.exorath.plugin.game.cakewars.team.CWTeamManager;
import com.exorath.service.currency.api.CurrencyServiceAPI;
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
public class Main extends JavaPlugin {
    public static final String CRUMBS_CURRENCY = "Crumbs";
    private static Main instance;
    private BaseGameAPI baseGameAPI;
    private ExoBaseAPI exoBaseAPI;
    private ConfigProvider configProvider;

    @Override
    public void onEnable() {
        Main.instance = this;
        this.baseGameAPI = BaseGameAPI.getInstance();
        this.exoBaseAPI = ExoBaseAPI.getInstance();
        try {
            this.configProvider = new FileConfigProvider(new File(getDataFolder(), "config.json"));
        } catch (FileNotFoundException e) {
            Main.terminate(e.getMessage());
        }
        String flavor = exoBaseAPI.getManager(FlavorManager.class).getFlavor();
        ConfigurationSection flavorSection = getConfig().getConfigurationSection("flavors." + flavor);
        FileConfiguration mapConfig = baseGameAPI.getMapsManager().getGameMap().getConfiguration();
        ConfigurationSection mapFlavorSection = mapConfig.getConfigurationSection("flavors." + flavor);
        exoBaseAPI.registerManager(new PlayerManager());
        exoBaseAPI.registerManager(new CWTeamManager(baseGameAPI.getTeamAPI(), exoBaseAPI.getManager(PlayerManager.class), mapFlavorSection.getConfigurationSection("teams")));
        exoBaseAPI.registerManager(new SpawnersManager(mapFlavorSection.getConfigurationSection("spawners"), flavorSection.getConfigurationSection("spawnerTypes")));
        exoBaseAPI.registerManager(new StartTeleportManager(baseGameAPI.getTeamAPI()));
        exoBaseAPI.registerManager(new ShopManager(exoBaseAPI.getManager(ClickableEntitiesManager.class), baseGameAPI.getTeamAPI().getTeams(), flavorSection.getConfigurationSection("shop")));//depends on spawner
        exoBaseAPI.registerManager(new KitsManager(new KitServiceAPI(getKitServiceAddress()), configProvider.getKitPackageJson()));

        exoBaseAPI.registerManager(new RewardManager(new CurrencyServiceAPI(getCurrencyServiceAddress())));
        exoBaseAPI.registerManager(new GameProtectionManager(exoBaseAPI.getManager(PlayerManager.class)));
        exoBaseAPI.registerManager(new CakeManager(baseGameAPI.getTeamAPI()));
        exoBaseAPI.registerManager(new FinishManager());
        if (mapFlavorSection.contains("minPlayers"))
            baseGameAPI.getTeamAPI().addStartRule(new GlobalMinPlayersStartRule(baseGameAPI.getTeamAPI(), mapFlavorSection.getInt("minPlayers")));
        baseGameAPI.getStateManager().setState(State.WAITING_FOR_PLAYERS);
        exoBaseAPI.registerManager(new ScoreboardManager());
    }

    public static Main getInstance() {
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

    private String getCurrencyServiceAddress() {
        String address = System.getenv("CURRENCY_SERVICE_ADDRESS");
        if (address == null)
            Main.terminate("No CURRENCY_SERVICE_ADDRESS env found.");
        return address;
    }

    public MapsManager getMapsManager() {
        return baseGameAPI.getMapsManager();
    }
}
