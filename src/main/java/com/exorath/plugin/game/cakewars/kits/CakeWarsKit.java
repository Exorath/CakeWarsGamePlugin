package com.exorath.plugin.game.cakewars.kits;

import com.exorath.commons.ItemStackSerialize;
import com.exorath.plugin.game.cakewars.Main;
import com.exorath.service.kit.res.Kit;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by toonsev on 5/22/2017.
 */
public class CakeWarsKit extends Kit {
    public CakeWarsKit(String name, int crumbsCost, Collection<ItemStack> items) {
        super(name, loadCostsMap(crumbsCost), loadMeta(items));
    }

    public Collection<ItemStack> getItems(){
        if(!getMeta().has("items"))
            return new ArrayList<>(0);
        List<ItemStack> items = new ArrayList<>(getMeta().get("items").getAsJsonArray().size());
        getMeta().get("items").getAsJsonArray().forEach(jsonElement -> items.add(ItemStackSerialize.toItemStack(jsonElement.getAsJsonObject())));
        return items;
    }

    private static JsonObject loadMeta(Collection<ItemStack> items) {
        JsonObject meta = new JsonObject();
        JsonArray itemsArray = new JsonArray();
        items.forEach(is -> itemsArray.add(ItemStackSerialize.fromItemStack(is)));
        meta.add("items", itemsArray);
        return meta;
    }

    private static HashMap<String, Integer> loadCostsMap(int crumbsCost) {
        HashMap<String, Integer> costsMap = new HashMap<>();
        costsMap.put(Main.CRUMBS_CURRENCY, crumbsCost);
        return costsMap;
    }
}
