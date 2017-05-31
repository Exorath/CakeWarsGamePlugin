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
