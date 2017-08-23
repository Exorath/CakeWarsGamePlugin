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

import com.exorath.clickents.api.ClickableEntity;
import com.exorath.exoHUD.DisplayProperties;
import com.exorath.exoHUD.locations.row.HologramLocation;
import com.exorath.exoHUD.removers.NeverRemover;
import com.exorath.exoHUD.texts.ChatColorText;
import com.exorath.exoHUD.texts.PlainText;
import com.exorath.exoteams.Team;
import com.exorath.plugin.basegame.clickableEntities.ClickableEntitiesManager;
import com.exorath.plugin.base.manager.ListeningManager;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.basegame.state.StateChangeEvent;
import com.exorath.plugin.game.cakewars.Main;
import com.exorath.plugin.game.cakewars.team.CWTeam;
import io.reactivex.Observable;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

/**
 * Created by toonsev on 3/18/2017.
 */
public class ShopManager implements ListeningManager {
    private Collection<Team> teams;
    private ClickableEntitiesManager clickableEntitiesManager;
    private ConfigurationSection shopSection;
    private ShopMenu shopMenu;
    private boolean started = false;

    public ShopManager(ClickableEntitiesManager clickableEntitiesManager, Collection<Team> teams, ConfigurationSection shopSection) {
        this.clickableEntitiesManager = clickableEntitiesManager;
        this.teams = teams;
        this.shopSection = shopSection;
        this.shopMenu = loadMenu(shopSection);

    }

    private static ShopMenu loadMenu(ConfigurationSection section) {
        if (section == null)
            return new ShopMenu();
        ShopMenu menu = new ShopMenu();
        List<HashMap<String, Object>> directoryMaps = (List<HashMap<String, Object>>) section.getList("directories", new ArrayList<HashMap>());
        for (Map<String, Object> directoryMap : directoryMaps) {
            menu.addShopDirectory(ShopDirectory.load(menu, directoryMap));
        }
        return menu;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onStateChange(StateChangeEvent event) {
        if (started)
            return;
        if (event.getNewState() == State.STARTED) {
            started = true;
            teams.stream().filter(team -> team instanceof CWTeam)
                    .map(team -> (CWTeam) team)
                    .forEach(cwTeam -> loadPrimaryShop(cwTeam.getPrimaryShopLocation()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onSpawn(EntitySpawnEvent event) {
        if (event.getEntity().hasMetadata("shopEntity"))
            event.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory() instanceof MerchantInventory || event.getInventory().getHolder() instanceof Villager)
            event.setCancelled(true);
    }

    private void loadPrimaryShop(Location primaryShopLocation) {
        Villager entity = primaryShopLocation.getWorld().spawn(primaryShopLocation, Villager.class, villager -> {
            villager.setMetadata("doNotDespawn", new FixedMetadataValue(Main.getInstance(), ""));
            villager.setMetadata("shopEntity", new FixedMetadataValue(Main.getInstance(), ""));

        });
        loadShopHologram(entity.getLocation());
        entity.setAI(false);
        entity.setProfession(Villager.Profession.LIBRARIAN);
        ClickableEntity clickableEntity = clickableEntitiesManager.getClickEntAPI().makeClickable(entity);
        Observable<PlayerInteractAtEntityEvent> obs = clickableEntity.getInteractObservable();
        obs.subscribe(event -> {
            event.setCancelled(true);
            event.getPlayer().closeInventory();
            this.shopMenu.getMenu().open(event.getPlayer());
//            event.getPlayer().sendMessage("Let's see what this delay does.");
//            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
//            }, 60l);
        });
    }

    private Set<HologramLocation> locations = new HashSet<>();

    private void loadShopHologram(Location location) {
        HologramLocation hologramLocation = new HologramLocation(location.clone().add(0, 2.75d, 0));
        locations.add(hologramLocation);
        hologramLocation.addText(new ChatColorText(new PlainText("Item Shop")).color(ChatColor.GREEN), DisplayProperties.create(0, NeverRemover.never()));
        hologramLocation.addText(new ChatColorText(new PlainText("(Right Click)")).color(ChatColor.AQUA), DisplayProperties.create(-1, NeverRemover.never()));
    }
}
