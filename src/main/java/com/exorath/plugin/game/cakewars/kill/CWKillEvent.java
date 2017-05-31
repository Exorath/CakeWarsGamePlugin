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

package com.exorath.plugin.game.cakewars.kill;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by toonsev on 5/31/2017.
 */
public class CWKillEvent extends Event {
    private Player killer;
    private Player killed;
    private PlayerDeathEvent event;
    private static HandlerList handlerList = new HandlerList();


    public CWKillEvent(Player killer, Player killed, PlayerDeathEvent event) {
        this.killer = killer;
        this.killed = killed;
        this.event = event;
    }

    public Player getKiller() {
        return killer;
    }

    public Player getKilled() {
        return killed;
    }

    public PlayerDeathEvent getEvent() {
        return event;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
