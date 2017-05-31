package com.exorath.plugin.game.cakewars.victory;

import com.exorath.plugin.game.cakewars.Main;
import com.exorath.service.currency.api.CurrencyServiceAPI;
import com.exorath.victoryHandler.rewards.CurrencyReward;
import net.md_5.bungee.api.ChatColor;

/**
 * Created by toonsev on 5/31/2017.
 */
public class KillStreakReward extends CurrencyReward{
    private int kills;
    public KillStreakReward(CurrencyServiceAPI currencyServiceAPI) {
        super(null, currencyServiceAPI, Main.CRUMBS_CURRENCY, 0);
        setCurrencyColor(ChatColor.GOLD);
        setCurrencyName("Crumbs");
    }

    public void addKill(){
        kills++;
        setAmount(kills);
        setReason(kills + " Player Kill Streak");
    }
}
