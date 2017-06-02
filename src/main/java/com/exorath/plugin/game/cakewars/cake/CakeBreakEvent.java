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

import com.exorath.exoteams.Team;
import com.exorath.plugin.game.cakewars.players.CWPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by toonsev on 6/2/2017.
 */
public class CakeBreakEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    CWPlayer destroyer;
    Team cakeTeam;
    Block cakeBlock;

    public CakeBreakEvent(CWPlayer destroyer, Team cakeTeam, Block cakeBlock) {
        this.destroyer = destroyer;
        this.cakeTeam = cakeTeam;
        this.cakeBlock = cakeBlock;
    }

    public CWPlayer getDestroyer() {
        return destroyer;
    }

    public Team getCakeTeam() {
        return cakeTeam;
    }

    public Block getCakeBlock() {
        return cakeBlock;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
