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

import com.exorath.plugin.game.cakewars.team.CWTeam;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;
import java.util.List;

/**
 * Created by toonsev on 6/4/2017.
 */
public class GameFinishEvent extends Event {
    private Collection<CWTeam> victors;
    private List<CWTeam> lostTeams;


    public GameFinishEvent(Collection<CWTeam> victors, List<CWTeam> lostTeams) {
        this.victors = victors;
        this.lostTeams = lostTeams;
    }
    public Collection<CWTeam> getVictors() {
        return victors;
    }

    public List<CWTeam> getLostTeams() {
        return lostTeams;
    }

    private static HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
}
