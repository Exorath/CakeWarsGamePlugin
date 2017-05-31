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
