package com.exorath.plugin.game.cakewars.kill;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by toonsev on 5/31/2017.
 */
public class KillStreakEvent extends Event {
    private Player player;
    private int currentStreak;
    private static HandlerList handlerList = new HandlerList();

    public KillStreakEvent(Player player, int currentStreak) {
        this.player = player;
        this.currentStreak = currentStreak;
    }

    public Player getPlayer() {
        return player;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}