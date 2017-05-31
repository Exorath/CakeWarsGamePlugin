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

package com.exorath.plugin.game.cakewars.spawners;

import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.game.cakewars.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

/**
 * Created by toonsev on 3/17/2017.
 */
public class Spawner {
    private Location location;
    private SpawnerType type;
    private boolean started = false;

    public Spawner(Location location, SpawnerType type) {
        this.location = location;
        this.type = type;
    }

    public boolean start() {
        if (started)
            return false;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            if(BaseGameAPI.getInstance().getStateManager().getState() != State.STARTED)
                return;
            if (location.getWorld() != null) {
                location.getWorld().dropItem(location, new ItemStack(type.getMaterial()));
            } else
                System.out.println("Spawner: no world found");
        }, 0, type.getInterval());
        return true;
    }

    public Location getLocation() {
        return location;
    }

}
