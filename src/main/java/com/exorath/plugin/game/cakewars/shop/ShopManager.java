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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Collection;

/**
 * Created by toonsev on 3/18/2017.
 */
public class ShopManager implements ListeningManager {
    private Collection<Team> teams;
    private ClickableEntitiesManager clickableEntitiesManager;
    private boolean started = false;

    public ShopManager(ClickableEntitiesManager clickableEntitiesManager, Collection<Team> teams) {
        this.clickableEntitiesManager = clickableEntitiesManager;
        this.teams = teams;

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
        obs.subscribe(event -> event.getPlayer().sendMessage("clicked"));
    }
}
