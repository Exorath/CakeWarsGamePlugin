package com.exorath.plugin.game.cakewars.shop;

import com.exorath.clickents.api.ClickableEntity;
import com.exorath.exoteams.Team;
import com.exorath.plugin.basegame.clickableEntities.ClickableEntitiesManager;
import com.exorath.plugin.basegame.manager.ListeningManager;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.basegame.state.StateChangeEvent;
import com.exorath.plugin.game.cakewars.team.CWTeam;
import io.reactivex.Observable;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.*;

/**
 * depends on spawnermanager
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

    private static ShopMenu loadMenu(ConfigurationSection section){
        if(section == null)
            return new ShopMenu();
        ShopMenu menu = new ShopMenu();
        List<HashMap<String, Object>> directoryMaps = (List<HashMap<String, Object>>) section.getList("directories", new ArrayList<HashMap>());
        for(Map<String, Object> directoryMap : directoryMaps){
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

    private void loadPrimaryShop(Location primaryShopLocation) {
        Entity entity = primaryShopLocation.getWorld().spawnEntity(primaryShopLocation, EntityType.VILLAGER);
        ClickableEntity clickableEntity = clickableEntitiesManager.getClickEntAPI().makeClickable(entity);
        Observable<PlayerInteractAtEntityEvent> obs = clickableEntity.getInteractObservable();
        obs.subscribe(event -> this.shopMenu.getMenu().open(event.getPlayer()));
    }
}
