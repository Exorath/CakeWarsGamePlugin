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

package com.exorath.plugin.game.cakewars.protection;

import com.exorath.exoProtection.ProtectionListener;
import com.exorath.plugin.basegame.manager.ListeningManager;
import com.exorath.plugin.game.cakewars.Main;
import com.exorath.plugin.game.cakewars.players.CWPlayer;
import com.exorath.plugin.game.cakewars.players.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Created by toonsev on 6/2/2017.
 */
public class GameProtectionManager extends ProtectionListener implements ListeningManager {
    private PlayerManager playerManager;

    public GameProtectionManager(PlayerManager playerManager) {
        super(new GameProtectionConfiguration());
        this.playerManager = playerManager;
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.getBlock().setMetadata("byPlayer", new FixedMetadataValue(Main.getInstance(), true));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().hasMetadata("byPlayer"))
            event.setCancelled(false);
        else
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDmgEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player))
            return;
        CWPlayer cwDamager = playerManager.getPlayer((Player) event.getDamager());
        CWPlayer cwDamaged = playerManager.getPlayer((Player) event.getEntity());
        if (cwDamaged != null && cwDamager != null & cwDamager.getTeam() != null && cwDamaged.getTeam() != null)
            event.setCancelled(cwDamaged.getTeam().equals(cwDamager.getTeam()));
    }
}
