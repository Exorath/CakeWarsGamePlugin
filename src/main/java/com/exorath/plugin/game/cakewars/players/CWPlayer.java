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

import com.exorath.plugin.game.cakewars.team.CWTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by toonsev on 5/31/2017.
 */
public class CWPlayer {
    private Player player;
    private CWTeam team;
    private PlayerState state;

    public CWPlayer(Player player) {
        this.player = player;
    }

    public synchronized void setTeam(CWTeam team) {
        System.out.println("Set player team: " + team);
        this.team = team;
    }

    public synchronized CWTeam getTeam() {
        return team;
    }

    public synchronized void setState(PlayerState state) {
        PlayerState oldState = this.state;
        this.state = state;
        Bukkit.getPluginManager().callEvent(new PlayerStateChangeEvent(this, oldState, state));
    }

    public synchronized PlayerState getState() {
        return state;
    }

    public synchronized Player getPlayer() {
        return player;
    }


}
