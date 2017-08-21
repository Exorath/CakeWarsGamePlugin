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

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by toonsev on 6/4/2017.
 */
public class TeamPlayingChangeEvent extends Event {
    private CWTeam cwTeam;
    private boolean playing;

    public TeamPlayingChangeEvent(CWTeam cwTeam, boolean playing) {
        this.cwTeam = cwTeam;
        this.playing = playing;
    }

    public CWTeam getCwTeam() {
        return cwTeam;
    }

    public boolean isPlaying() {
        return playing;
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
