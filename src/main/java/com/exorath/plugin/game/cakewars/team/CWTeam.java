package com.exorath.plugin.game.cakewars.team;

import com.exorath.exoteams.Team;
import org.bukkit.Location;

/**
 * Created by toonsev on 3/15/2017.
 */
public class CWTeam extends Team {
    private Location spawnLocation;
    private Location cakeLocation;
    private Location primaryShopLocation;

    public CWTeam(Location spawnLocation, Location cakeLocation, Location primaryShopLocation, int maxPlayers) {
        this.spawnLocation = spawnLocation;
        this.cakeLocation = cakeLocation;
        this.primaryShopLocation = primaryShopLocation;
        setMaxPlayers(maxPlayers);
    }

    public Location getCakeLocation() {
        return cakeLocation;
    }

    public Location getPrimaryShopLocation() {
        return primaryShopLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }
}
