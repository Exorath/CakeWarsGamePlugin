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

package com.exorath.plugin.game.cakewars.kill;

import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.basegame.manager.ListeningManager;
import com.exorath.plugin.game.cakewars.players.CWPlayer;
import com.exorath.plugin.game.cakewars.players.PlayerManager;
import com.exorath.plugin.game.cakewars.players.PlayerState;
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
        CWPlayer cwPlayer = BaseGameAPI.getInstance().getManager(PlayerManager.class).getPlayer(player);
        if(cwPlayer == null || cwPlayer.getTeam() == null || !cwPlayer.getTeam().isEggAlive()){
            cwPlayer.setState(PlayerState.SPECTATOR);
            player.spigot().respawn();
            return;
        }
        player.spigot().respawn();
    }


}
