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

import com.exorath.plugin.basegame.manager.ListeningManager;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.basegame.state.StateChangeEvent;
import com.exorath.plugin.game.cakewars.cake.CakeBreakEvent;
import com.exorath.plugin.game.cakewars.team.CWTeam;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toonsev on 6/3/2017.
 */
public class FinishManager implements ListeningManager {
    private List<CWTeam> lostTeams = new ArrayList<>();//First entries lost first

    @EventHandler
    public void onCakeDestroy(CakeBreakEvent event){
        //
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        //check victory and whether the team's cake should be destroyed
    }


    @EventHandler
    public void onStateChange(StateChangeEvent event){
        if(event.getNewState() == State.STARTED){
            //Start force checking whether the game should finish
        }
    }

    @EventHandler
    public void onPlayerDieEvent(PlayerDeathEvent event){
        //If the player is the last person from his team that is alive when the egg is gone, check if the game can finish;
    }
}