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

package com.exorath.plugin.game.cakewars.players;

import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.basegame.manager.ListeningManager;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.basegame.state.StateChangeEvent;
import com.exorath.plugin.basegame.team.TeamManager;
import com.exorath.plugin.game.cakewars.Main;
import com.exorath.plugin.game.cakewars.team.CWTeam;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by toonsev on 5/31/2017.
 */
public class PlayerManager implements ListeningManager {
    private static final int RESPAWN_SECONDS = 8;
    private Map<Player, CWPlayer> players = new HashMap<>();

    public PlayerManager() {
        BaseGameAPI.getInstance().getTeamAPI().getPlayerJoinTeamObservable().subscribe(teamJoinEvent ->
                getPlayer(TeamManager.getPlayer(teamJoinEvent.getPlayer())).setTeam((CWTeam) teamJoinEvent.getTeam()));
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        players.put(event.getPlayer(), new CWPlayer(event.getPlayer()));

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLeave(PlayerQuitEvent event) {
        players.remove(event.getPlayer());
    }

    @EventHandler
    public void onGameStateChange(StateChangeEvent event) {
        if (event.getNewState() == State.STARTED) {
            BaseGameAPI.getInstance().getTeamAPI().getTeams().forEach(team ->
                    team.getPlayers().forEach(player -> getPlayer(TeamManager.getPlayer(player)).setState(PlayerState.PLAYING)));
        }
    }

    public CWPlayer getPlayer(Player player) {
        CWPlayer cwPlayer = players.get(player);
        if (cwPlayer == null) {
            cwPlayer = new CWPlayer(player);
            players.put(player, cwPlayer);
        }
        return cwPlayer;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        CWPlayer cwPlayer = getPlayer(event.getEntity());
        event.getEntity().setGameMode(GameMode.SPECTATOR);
        event.getEntity().spigot().respawn();
        if (cwPlayer.getTeam() == null || !cwPlayer.getTeam().isEggAlive())
            cwPlayer.setState(PlayerState.SPECTATOR);
        else if (cwPlayer.getState() == PlayerState.PLAYING) {
            cwPlayer.setState(PlayerState.RESPAWNING);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                if (cwPlayer.getState() == PlayerState.RESPAWNING) {
                    cwPlayer.setState(PlayerState.PLAYING);
                    cwPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
                    if (cwPlayer.getTeam() != null)
                        cwPlayer.getPlayer().teleport(cwPlayer.getTeam().getSpawnLocation());
                    else
                        cwPlayer.getPlayer().sendMessage(ChatColor.RED + "You do not have a team, can't spawn...");
                }
            }, RESPAWN_SECONDS * 20);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            CWPlayer cwPlayer = players.get(event.getEntity());
            if (cwPlayer != null && cwPlayer.getState() != PlayerState.PLAYING)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void respawnEvent(PlayerRespawnEvent event) {
        CWPlayer cwPlayer = players.get(event.getPlayer());
        Location location = event.getPlayer().getLocation();
        if (location.getY() < -60)
            location = cwPlayer.getTeam().getSpawnLocation();
        if (cwPlayer != null && cwPlayer.getState() == PlayerState.PLAYING)
            event.getPlayer().sendMessage(ChatColor.GRAY + "You " + ChatColor.RED + "died" + ChatColor.GRAY + ". Respawning in " + ChatColor.RED + RESPAWN_SECONDS + " seconds" + ChatColor.GRAY + ".");
        event.setRespawnLocation(location);
    }
}
