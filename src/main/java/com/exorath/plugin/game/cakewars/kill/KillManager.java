package com.exorath.plugin.game.cakewars.kill;

import com.exorath.plugin.basegame.manager.ListeningManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

/**
 * Created by toonsev on 5/31/2017.
 */
public class KillManager implements ListeningManager {
    private HashMap<Player, Player> lastDamagerMap = new HashMap<>();

    @EventHandler
    public void onEntityDamageEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER && event.getEntity().getType() != EntityType.PLAYER)
            return;
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player))
            return;
        lastDamagerMap.put((Player) event.getEntity(), (Player) event.getDamager());

    }

    @EventHandler
    public void onPlayerDieEvent(PlayerDeathEvent event) {
        Location location = event.getEntity().getLocation();
        event.getEntity().spigot().respawn();
        event.getEntity().teleport(location);
        if (!lastDamagerMap.containsKey(event.getEntity()))
            return;
        CWKillEvent killEvent = new CWKillEvent(event.getEntity(), lastDamagerMap.get(event.getEntity()), event);
        Bukkit.getPluginManager().callEvent(killEvent);

    }


}
