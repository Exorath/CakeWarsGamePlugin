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

import com.exorath.plugin.game.cakewars.Main;
import com.exorath.service.currency.api.CurrencyServiceAPI;
import com.exorath.victoryHandler.rewards.CurrencyReward;
import net.md_5.bungee.api.ChatColor;

/**
 * Created by toonsev on 5/31/2017.
 */
public class KillsReward extends CurrencyReward{
    public static final int CRUMBS_PER_KILL = 2;
    private int kills;
    public KillsReward(CurrencyServiceAPI currencyServiceAPI) {
        super(null, currencyServiceAPI, Main.CRUMBS_CURRENCY, 0);
        setCurrencyColor(ChatColor.GOLD);
        setCurrencyName("Crumbs");
    }

    public void addKill(){
        kills++;
        setAmount(kills*CRUMBS_PER_KILL);
        setReason("Killing " + kills + " Players");
    }
}
