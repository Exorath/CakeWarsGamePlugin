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
    private HashMap<Player, Integer> killStreaks = new HashMap<>();

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
        respawn(event.getEntity());
        killStreaks.remove(event.getEntity());
        if (!lastDamagerMap.containsKey(event.getEntity()))
            return;
        Player killer = lastDamagerMap.get(event.getEntity());
        addStreak(killer);
        Bukkit.getPluginManager().callEvent(new CWKillEvent(killer, event.getEntity(), event));
    }

    private void addStreak(Player player) {
        if (killStreaks.containsKey(player))
            killStreaks.put(player, killStreaks.get(player) + 1);
        else
            killStreaks.put(player, 1);
        if (killStreaks.get(player) >= 2)
            Bukkit.getPluginManager().callEvent(new KillStreakEvent(player, killStreaks.get(player)));
    }

    private void respawn(Player player) {
        Location location = player.getLocation();
        player.spigot().respawn();
        player.teleport(location);
    }


}
