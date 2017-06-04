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

package com.exorath.plugin.game.cakewars.finish;

import com.exorath.exoteams.Team;
import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.basegame.manager.ListeningManager;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.basegame.state.StateChangeEvent;
import com.exorath.plugin.game.cakewars.Main;
import com.exorath.plugin.game.cakewars.cake.CakeBreakEvent;
import com.exorath.plugin.game.cakewars.players.CWPlayer;
import com.exorath.plugin.game.cakewars.players.PlayerManager;
import com.exorath.plugin.game.cakewars.team.CWTeam;
import com.exorath.plugin.game.cakewars.team.TeamPlayingChangeEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by toonsev on 6/3/2017.
 */
public class FinishManager implements ListeningManager {
    private List<CWTeam> lostTeams = new ArrayList<>();//First entries lost first

    //not sure when this would return true but whatever
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCakeDestroy(CakeBreakEvent event) {
        CWTeam team = (CWTeam) event.getCakeTeam();
        if (team.isPlaying() && team.shouldLose())
            team.setPlaying(false);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerLeave(PlayerQuitEvent event) {
        CWPlayer cwPlayer = getPlayer(event.getPlayer());

        if (cwPlayer.getTeam() != null && cwPlayer.getTeam().isPlaying() && cwPlayer.getTeam().shouldLose())
            cwPlayer.getTeam().setPlaying(false);
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onStateChange(StateChangeEvent event) {
        if (event.getNewState() == State.STARTED) {
            long playing = BaseGameAPI.getInstance().getTeamAPI().getTeams().stream().map(team -> (CWTeam) team)
                    .filter(cwTeam -> cwTeam.isPlaying()).count();
            if (playing <= 1) {
                finish();
                return;
            }
            BaseGameAPI.getInstance().getTeamAPI().getTeams().stream().map(team -> (CWTeam) team)
                    .filter(cwTeam -> cwTeam.shouldLose() && cwTeam.isPlaying())
                    .forEach(team -> team.setPlaying(false));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDieEvent(PlayerDeathEvent event) {
        CWPlayer cwPlayer = getPlayer(event.getEntity());
        if(cwPlayer.getTeam() == null){
            System.out.println("Player team is null?");
            return;
        }
        if (cwPlayer.getTeam().isPlaying() && cwPlayer.getTeam().shouldLose())
            cwPlayer.getTeam().setPlaying(false);

    }

    @EventHandler
    public void onTeamPlayingChange(TeamPlayingChangeEvent event) {
        if (!event.isPlaying()) {
            if (!lostTeams.contains(event.getCwTeam()))
                lostTeams.add(event.getCwTeam());
            testFinish(event);
        }
    }

    @EventHandler
    public void onFinishEvent(GameFinishEvent event) {
        Bukkit.broadcastMessage(ChatColor.GREEN + "Game finished.");
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            Bukkit.broadcastMessage(ChatColor.RED + "Server terminating.");
            Main.terminate("Game finished successfully.");
        }, 200l);
    }

    private void testFinish(TeamPlayingChangeEvent event) {
        if (!event.isPlaying()) {
            int playing = 0;
            for (Team team : BaseGameAPI.getInstance().getTeamAPI().getTeams()) {
                CWTeam cwTeam = (CWTeam) team;
                if (cwTeam.isPlaying()) {
                    playing++;
                    if (playing >= 2)
                        return;
                }
            }
            finish();
        }
    }

    private void finish() {
        Set<CWTeam> victors = BaseGameAPI.getInstance().getTeamAPI().getTeams().stream().map(team -> (CWTeam) team)
                .filter(cwTeam -> cwTeam.isPlaying()).collect(Collectors.toSet());
        Bukkit.getPluginManager().callEvent(new GameFinishEvent(victors, lostTeams));
    }

    private CWPlayer getPlayer(Player player) {
        return BaseGameAPI.getInstance().getManager(PlayerManager.class).getPlayer(player);
    }
}