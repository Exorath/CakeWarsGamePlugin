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

package com.exorath.plugin.game.cakewars.rewards;

import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.basegame.manager.ListeningManager;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.basegame.state.StateChangeEvent;
import com.exorath.plugin.game.cakewars.kill.CWKillEvent;
import com.exorath.plugin.game.cakewars.kill.KillStreakEvent;
import com.exorath.service.currency.api.CurrencyServiceAPI;
import com.exorath.victoryHandler.VictoryPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Optional;

/**
 * Created by toonsev on 5/31/2017.
 */
public class RewardManager implements ListeningManager {
    private CurrencyServiceAPI currencyServiceAPI;


    public RewardManager(CurrencyServiceAPI currencyServiceAPI) {
        this.currencyServiceAPI = currencyServiceAPI;
    }

    @EventHandler
    public void onStateChange(StateChangeEvent event) {
        if (event.getNewState() == State.STOPPING)
            BaseGameAPI.getInstance().getVictoryManager().getVictoryHandlerAPI().endGame();
    }

    @EventHandler
    public void onKill(CWKillEvent event) {
        VictoryPlayer victoryPlayer = BaseGameAPI.getInstance().getVictoryManager().getVictoryHandlerAPI().getPlayer(event.getKiller());
        Optional<KillsReward> killsRewardOptional = victoryPlayer.getRewards().stream().filter(attribute -> attribute instanceof KillsReward).map(attribute -> (KillsReward) attribute).findFirst();
        killsRewardOptional.orElseGet(() -> {
            KillsReward killsReward = new KillsReward(currencyServiceAPI);
            victoryPlayer.addReward(killsReward);
            return killsReward;
        }).addKill();

    }

    @EventHandler
    public void onKillStream(KillStreakEvent event) {
        VictoryPlayer victoryPlayer = BaseGameAPI.getInstance().getVictoryManager().getVictoryHandlerAPI().getPlayer(event.getPlayer());
        Optional<KillStreakReward> killsRewardOptional = victoryPlayer.getRewards().stream().filter(attribute -> attribute instanceof KillStreakReward).map(attribute -> (KillStreakReward) attribute).findFirst();
        KillStreakReward reward = killsRewardOptional.orElseGet(() -> {
            KillStreakReward killStreakReward = new KillStreakReward(currencyServiceAPI);
            victoryPlayer.addReward(killStreakReward);
            return killStreakReward;
        });
        if (reward.getKills() < event.getCurrentStreak())
            reward.setKills(event.getCurrentStreak());
    }

    private void sendCrumbsMessage(Player player, int crumbs) {
        player.sendMessage(ChatColor.GOLD + "+" + crumbs + " Crumbs");
    }
}
