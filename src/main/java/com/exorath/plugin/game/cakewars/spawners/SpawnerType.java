package com.exorath.plugin.game.cakewars.spawners;

import org.bukkit.Material;

/**
 * Created by toonsev on 3/17/2017.
 */
public class SpawnerType {
    private Material material;
    private long interval;

    public SpawnerType(Material material, long interval) {
        this.material = material;
        this.interval = interval;
    }

    public long getInterval() {
        return interval;
    }

    public Material getMaterial() {
        return material;
    }
}
