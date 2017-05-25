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
