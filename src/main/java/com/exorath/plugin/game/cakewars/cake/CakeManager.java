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

package com.exorath.plugin.game.cakewars.cake;

import com.exorath.exoteams.Team;
import com.exorath.exoteams.TeamAPI;
import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.basegame.manager.ListeningManager;
import com.exorath.plugin.basegame.team.TeamManager;
import com.exorath.plugin.game.cakewars.Main;
import com.exorath.plugin.game.cakewars.players.CWPlayer;
import com.exorath.plugin.game.cakewars.players.PlayerManager;
import com.exorath.plugin.game.cakewars.team.CWTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by toonsev on 6/2/2017.
 */
public class CakeManager implements ListeningManager {
    private Map<Block, Team> teamByCake = new HashMap<>();

    public CakeManager(TeamAPI teamAPI) {
        teamAPI.getTeams().stream().map(team -> (CWTeam) team).forEach(team -> {
            Location cakeLocation = team.getCakeLocation();
            if (cakeLocation == null)
                Main.terminate("No cakeLocation set for a team");
            cakeLocation.getBlock().setType(Material.CAKE_BLOCK);
            cakeLocation.getBlock().setMetadata("cake", new FixedMetadataValue(Main.getInstance(), true));
        });
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().hasMetadata("cake"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.getBlock().setMetadata("byPlayer", new FixedMetadataValue(Main.getInstance(), true));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().hasMetadata("cake")) {
            handleCakeBreak(event);
            return;
        }
        if (event.getBlock().hasMetadata("byPlayer"))
            event.setCancelled(false);
        else
            event.setCancelled(true);
    }

    private void handleCakeBreak(BlockBreakEvent event) {
        CWPlayer cwPlayer = BaseGameAPI.getInstance().getManager(PlayerManager.class).getPlayer(event.getPlayer());
        if (cwPlayer == null || cwPlayer.getTeam() == null) {
            event.setCancelled(true);
            return;
        }
        CWTeam blockTeam = (CWTeam) teamByCake.get(event.getBlock());
        if (blockTeam != null && !blockTeam.equals(cwPlayer.getTeam())) {
            blockTeam.setEggAlive(false);
            Bukkit.getPluginManager().callEvent(new CakeBreakEvent(cwPlayer, cwPlayer.getTeam(), event.getBlock()));
            Bukkit.broadcastMessage(ChatColor.GREEN + "Team " + blockTeam.getName() + ChatColor.GREEN + " egg has been destroyed.");
            blockTeam.getPlayers().forEach(teamPlayer -> TeamManager.getPlayer(teamPlayer).sendMessage(ChatColor.RED + "Your egg has been destroyed, you will no longer respawn."));

            event.setCancelled(false);
        }
    }
}
