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

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by toonsev on 5/31/2017.
 */
public class PlayerStateChangeEvent extends Event {
    private CWPlayer player;
    private PlayerState oldState;
    private PlayerState newState;

    public PlayerStateChangeEvent(CWPlayer player, PlayerState oldState, PlayerState newState) {
        this.player = player;
        this.oldState = oldState;
        this.newState = newState;
    }

    private static HandlerList handlers = new HandlerList();

    public CWPlayer getPlayer() {
        return player;
    }

    public PlayerState getOldState() {
        return oldState;
    }

    public PlayerState getNewState() {
        return newState;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
}
