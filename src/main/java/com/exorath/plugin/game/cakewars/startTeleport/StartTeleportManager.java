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

package com.exorath.plugin.game.cakewars.startTeleport;

import com.exorath.exoteams.Team;
import com.exorath.exoteams.TeamAPI;
import com.exorath.plugin.base.manager.ListeningManager;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.basegame.state.StateChangeEvent;
import com.exorath.plugin.basegame.team.TeamManager;
import com.exorath.plugin.game.cakewars.team.CWTeam;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;

/**
 * Created by toonsev on 3/18/2017.
 */
public class StartTeleportManager implements ListeningManager {
    private TeamAPI teamAPI;
    public StartTeleportManager(TeamAPI teamAPI) {
        this.teamAPI = teamAPI;
    }

    @EventHandler
    public void onStateChange(StateChangeEvent event){
        if(event.getNewState() == State.STARTED){
            for(Team team : teamAPI.getTeams()){
                if(team instanceof CWTeam){
                    CWTeam cwTeam = (CWTeam) team;
                    Location spawn = cwTeam.getSpawnLocation();
                    cwTeam.getPlayers().forEach(player -> TeamManager.getPlayer(player).teleport(spawn));
                }
            }
        }
    }
}
