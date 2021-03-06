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
import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.base.manager.ListeningManager;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.game.cakewars.Main;
import com.exorath.plugin.game.cakewars.players.CWPlayer;
import com.exorath.plugin.game.cakewars.players.PlayerState;
import com.exorath.plugin.game.cakewars.players.PlayerStateChangeEvent;
import com.exorath.service.kit.api.KitServiceAPI;
import com.exorath.service.kit.res.Kit;
import com.exorath.service.kit.res.KitPackage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;


/**
 * Created by toonsev on 5/22/2017.
 */
public class KitsManager implements ListeningManager {
    private static final Gson GSON = new Gson();
    private KitServiceAPI kitServiceAPI;
    private KitPackage kitPackage;

    public KitsManager(KitServiceAPI kitServiceAPI, JsonObject kitPackageJson) {
        this.kitServiceAPI = kitServiceAPI;
        this.kitPackage = getKits(kitPackageJson);
        //Update kitPackage with kits from game
        kitServiceAPI.updatePackage("CW", kitPackage);
    }

    private KitPackage getKits(JsonObject kitPackage) {
        return GSON.fromJson(kitPackage, KitPackage.class);
    }

    private static ItemStack[] getItems(Kit kit) {
        if (!kit.getMeta().has("items"))
            return new ItemStack[]{};
        ItemStack[] items = new ItemStack[kit.getMeta().get("items").getAsJsonArray().size()];
        JsonArray jsonArray = kit.getMeta().get("items").getAsJsonArray();
        for (int i = 0; i < items.length; i++)
            items[i] = ItemStackSerialize.toItemStack(jsonArray.get(i).getAsJsonObject());
        return items;
    }

    @EventHandler
    public void onPlayerStateChange(PlayerStateChangeEvent event) {
        if (event.getNewState() == PlayerState.PLAYING) {
            if (BaseGameAPI.getInstance().getStateManager().getState() == State.STARTED)
                updatePlayer(event.getPlayer());
        }
    }

    private void updatePlayer(CWPlayer cwPlayer) {
        cwPlayer.getPlayer().getInventory().clear();
        String uuid = cwPlayer.getPlayer().getUniqueId().toString();
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            String kitId = kitServiceAPI.getCurrentKit("CW", uuid).getKit();
            if (kitId == null)
                return;
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                Kit kit = kitPackage.getKits().get(kitId);
                if (kit == null)
                    cwPlayer.getPlayer().sendMessage(ChatColor.RED + "You're selected kit no longer exists.");
                else
                    cwPlayer.getPlayer().getInventory().addItem(getItems(kit));
            });
        });
    }

}
