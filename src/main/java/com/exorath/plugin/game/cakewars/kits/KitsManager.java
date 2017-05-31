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

package com.exorath.plugin.game.cakewars.kits;

import com.exorath.commons.ItemStackSerialize;
import com.exorath.plugin.basegame.manager.ListeningManager;
import com.exorath.service.kit.api.KitServiceAPI;
import com.exorath.service.kit.res.Kit;
import com.exorath.service.kit.res.KitPackage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by toonsev on 5/22/2017.
 */
public class KitsManager implements ListeningManager{
    private static final Gson GSON = new Gson();
    private KitServiceAPI kitServiceAPI;
    private KitPackage kitPackage;

    public KitsManager(KitServiceAPI kitServiceAPI, JsonObject kitPackageJson) {
        this.kitServiceAPI = kitServiceAPI;
        this.kitPackage = getKits(kitPackageJson);
        //Update kitPackage with kits from game
        kitServiceAPI.updatePackage("CW", kitPackage);
    }

    private KitPackage getKits(JsonObject kitPackage){
        return GSON.fromJson(kitPackage, KitPackage.class);
    }
    private static Collection<ItemStack> getItems(Kit kit){
        if(!kit.getMeta().has("items"))
            return new ArrayList<>(0);
        List<ItemStack> items = new ArrayList<>(kit.getMeta().get("items").getAsJsonArray().size());
        kit.getMeta().get("items").getAsJsonArray().forEach(jsonElement -> items.add(ItemStackSerialize.toItemStack(jsonElement.getAsJsonObject())));
        return items;
    }

}
