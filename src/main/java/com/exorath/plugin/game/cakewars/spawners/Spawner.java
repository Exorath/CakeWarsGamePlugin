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
