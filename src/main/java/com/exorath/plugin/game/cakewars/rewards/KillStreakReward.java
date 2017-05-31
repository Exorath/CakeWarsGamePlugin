package com.exorath.plugin.game.cakewars.rewards;

import com.exorath.plugin.game.cakewars.Main;
import com.exorath.service.currency.api.CurrencyServiceAPI;
import com.exorath.victoryHandler.rewards.CurrencyReward;
import net.md_5.bungee.api.ChatColor;

/**
 * Created by toonsev on 5/31/2017.
 */
public class KillStreakReward extends CurrencyReward{
    public static final int CRUMBS_MULTIPLIER = 1;
    private int kills;
    public KillStreakReward(CurrencyServiceAPI currencyServiceAPI) {
        super(null, currencyServiceAPI, Main.CRUMBS_CURRENCY, 0);
        setCurrencyColor(ChatColor.GOLD);
        setCurrencyName("Crumbs");
    }

    public void setKills(Integer kills){
        this.kills = kills;
        setAmount(kills);
        setReason(kills + " Player Kill Streak");
    }

    public int getKills() {
        return kills;
    }
}
