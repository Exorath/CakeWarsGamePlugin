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

import com.exorath.exoHUD.DisplayProperties;
import com.exorath.exoHUD.locations.row.HologramLocation;
import com.exorath.exoHUD.removers.NeverRemover;
import com.exorath.exoHUD.texts.ChatColorText;
import com.exorath.exoHUD.texts.PlainText;
import com.exorath.exoteams.TeamAPI;
import com.exorath.plugin.base.ExoBaseAPI;
import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.base.manager.ListeningManager;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.basegame.state.StateChangeEvent;
import com.exorath.plugin.basegame.team.TeamManager;
import com.exorath.plugin.game.cakewars.Main;
import com.exorath.plugin.game.cakewars.players.CWPlayer;
import com.exorath.plugin.game.cakewars.players.PlayerManager;
import com.exorath.plugin.game.cakewars.team.CWTeam;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by toonsev on 6/2/2017.
 */
public class CakeManager implements ListeningManager {
    private TeamAPI teamAPI;
    private Map<Block, CWTeam> teamByCake = new HashMap<>();
    private Map<CWTeam, HologramLocation> hologramsByTeam = new HashMap<>();

    public CakeManager(TeamAPI teamAPI) {
        this.teamAPI = teamAPI;
        teamAPI.getTeams().stream().map(team -> (CWTeam) team).forEach(team -> {
            Location cakeLocation = team.getCakeLocation();
            if (cakeLocation == null)
                Main.terminate("No cakeLocation set for a team");
            teamByCake.put(cakeLocation.getBlock(), team);
        });
    }

    @EventHandler
    public void onStateChange(StateChangeEvent event) {
        if (event.getNewState() == State.STARTED) {
            teamByCake.forEach((block, team) -> {
                block.setType(Material.CAKE_BLOCK, false);
                block.setMetadata("cake", new FixedMetadataValue(Main.getInstance(), true));
                hologramsByTeam.put(team, new HologramLocation(block.getLocation().clone().add(0.5d, 1.5d, 0.5d)));
                hologramsByTeam.get(team).addText(new PlainText(team.getName() + "'s"), DisplayProperties.create(0, NeverRemover.never()));
                hologramsByTeam.get(team).addText(ChatColorText.markup(new PlainText("Egg")).color(ChatColor.GRAY), DisplayProperties.create(-1, NeverRemover.never()));
            });
        }
    }

    @EventHandler
    public void onPhysicsEvent(BlockPhysicsEvent event) {
        if (teamByCake.containsKey(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getAction() != Action.LEFT_CLICK_BLOCK && event.getClickedBlock().hasMetadata("cake"))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        teamByCake.forEach((block, team) -> {
            if (block.getLocation().equals(event.getBlock().getLocation()))
                System.out.println("Cake matches location");
        });
        if (teamByCake.containsKey(event.getBlock())) {
            System.out.println("cake has metadata");
            handleCakeBreak(event);
            return;
        }
        //TODO MOVE THIS
    }


    private void handleCakeBreak(BlockBreakEvent event) {
        CWPlayer cwPlayer = ExoBaseAPI.getInstance().getManager(PlayerManager.class).getPlayer(event.getPlayer());
        if (cwPlayer == null || cwPlayer.getTeam() == null) {
            System.out.println("Breaker does not have a team");
            event.setCancelled(true);
            return;
        }
        CWTeam blockTeam = teamByCake.get(event.getBlock());
        if (blockTeam != null && !blockTeam.equals(cwPlayer.getTeam()) && blockTeam.isEggAlive()) {
            blockTeam.setEggAlive(false);
            Bukkit.getPluginManager().callEvent(new CakeBreakEvent(cwPlayer, cwPlayer.getTeam(), event.getBlock()));
            Bukkit.broadcastMessage(ChatColor.GREEN + "Team " + blockTeam.getName() + ChatColor.GREEN + " egg has been destroyed.");
            blockTeam.getPlayers().forEach(teamPlayer -> TeamManager.getPlayer(teamPlayer).sendMessage(ChatColor.RED + "Your egg has been destroyed, you will no longer respawn."));
            event.setCancelled(false);
            hologramsByTeam.get(blockTeam).addText(ChatColorText.markup(PlainText.plain("Destroyed")).color(ChatColor.RED), DisplayProperties.create(-10, NeverRemover.never()));
        }else
            event.setCancelled(true);
    }

}
