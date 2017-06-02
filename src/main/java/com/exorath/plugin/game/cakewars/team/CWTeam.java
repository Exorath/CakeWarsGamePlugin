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

import com.exorath.exoteams.Team;
import org.bukkit.Location;

/**
 * Created by toonsev on 3/15/2017.
 */
public class CWTeam extends Team {
    private boolean eggAlive = true;
    private String name;
    private Location spawnLocation;
    private Location cakeLocation;
    private Location primaryShopLocation;

    public CWTeam(String name, Location spawnLocation, Location cakeLocation, Location primaryShopLocation, int maxPlayers) {
        this.name = name;
        this.spawnLocation = spawnLocation;
        this.cakeLocation = cakeLocation;
        this.primaryShopLocation = primaryShopLocation;
        setMaxPlayers(maxPlayers);
    }

    public void setEggAlive(boolean eggAlive) {
        this.eggAlive = eggAlive;
    }

    public boolean isEggAlive() {
        return eggAlive;
    }

    public String getName() {
        return name;
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
