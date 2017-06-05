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

package com.exorath.plugin.game.cakewars.team;

import com.exorath.exoteams.Team;
import com.exorath.exoteams.player.TeamPlayer;
import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.basegame.team.TeamManager;
import com.exorath.plugin.game.cakewars.players.CWPlayer;
import com.exorath.plugin.game.cakewars.players.PlayerManager;
import com.exorath.plugin.game.cakewars.players.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by toonsev on 3/15/2017.
 */
public class CWTeam extends Team {
    private boolean eggAlive = true;
    private boolean playing = false;
    private String name;
    private Location spawnLocation;
    private Location cakeLocation;
    private Location primaryShopLocation;

    public CWTeam(String name, Location spawnLocation, Location cakeLocation, Location primaryShopLocation, int maxPlayers) {
        this.name = name;
        this.spawnLocation = spawnLocation;
        this.cakeLocation = cakeLocation;
        this.primaryShopLocation = primaryShopLocation;
        setMaxPlayers(maxPlayers);
    }

    public void setPlaying(boolean playing) {
        if (this.playing == playing)
            return;
        System.out.println(this + " is now playing.");
        this.playing = playing;
        Bukkit.getPluginManager().callEvent(new TeamPlayingChangeEvent(this, playing));
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean shouldLose() {
        for (TeamPlayer teamPlayer : getPlayers()) {
            CWPlayer cwPlayer = BaseGameAPI.getInstance().getManager(PlayerManager.class).getPlayer(TeamManager.getPlayer(teamPlayer));
            if (cwPlayer.getPlayer().isOnline() && cwPlayer.getState() != PlayerState.SPECTATOR)
                return false;
        }
        return true;
    }
    public boolean shouldLose(Player player) {
        for (TeamPlayer teamPlayer : getPlayers()) {
            CWPlayer cwPlayer = BaseGameAPI.getInstance().getManager(PlayerManager.class).getPlayer(TeamManager.getPlayer(teamPlayer));
            if (cwPlayer.getPlayer() != player && cwPlayer.getPlayer().isOnline() && cwPlayer.getState() != PlayerState.SPECTATOR)
                return false;
        }
        return true;
    }
    public void setEggAlive(boolean eggAlive) {
        this.eggAlive = eggAlive;
    }

    public boolean isEggAlive() {
        return eggAlive;
    }

    public String getName() {
        return name;
    }

    public Location getCakeLocation() {
        return cakeLocation;
    }

    public Location getPrimaryShopLocation() {
        return primaryShopLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }
}
