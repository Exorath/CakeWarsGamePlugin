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

    public void setTeam(CWTeam team) {
        this.team = team;
    }

    public CWTeam getTeam() {
        return team;
    }

    public void setState(PlayerState state) {
        PlayerState oldState = this.state;
        this.state = state;
        Bukkit.getPluginManager().callEvent(new PlayerStateChangeEvent(this, oldState, state));
    }

    public PlayerState getState() {
        return state;
    }

    public Player getPlayer() {
        return player;
    }


}
