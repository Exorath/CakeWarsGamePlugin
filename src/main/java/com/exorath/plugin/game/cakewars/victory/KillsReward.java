package com.exorath.plugin.game.cakewars.victory;

import com.exorath.plugin.game.cakewars.Main;
import com.exorath.service.currency.api.CurrencyServiceAPI;
import com.exorath.victoryHandler.rewards.CurrencyReward;
import net.md_5.bungee.api.ChatColor;

/**
 * Created by toonsev on 5/31/2017.
 */
public class KillsReward extends CurrencyReward{
    private static final int CRUMBS_PER_KILL = 2;
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
