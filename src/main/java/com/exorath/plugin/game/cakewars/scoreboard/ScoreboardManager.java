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

package com.exorath.plugin.game.cakewars.scoreboard;

import com.exorath.exoHUD.DisplayProperties;
import com.exorath.exoHUD.locations.row.ScoreboardLocation;
import com.exorath.exoHUD.plugin.HudAPI;
import com.exorath.exoHUD.texts.ChatColorText;
import com.exorath.plugin.base.manager.ListeningManager;
import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.game.cakewars.team.CWTeam;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.exorath.exoHUD.removers.NeverRemover.never;
import static com.exorath.exoHUD.texts.PlainText.plain;


/**
 * Created by toonsev on 6/5/2017.
 */
public class ScoreboardManager implements ListeningManager {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        ScoreboardLocation scoreboardLocation = HudAPI.getInstance().getHudPlayer(event.getPlayer()).getScoreboardLocation();
        scoreboardLocation.addTitle(ChatColorText.markup(plain("CAKEWARS")).color(ChatColor.GREEN).bold(true), DisplayProperties.create(0, never()));
        BaseGameAPI.getInstance().getTeamAPI().getTeams().stream().map(team -> (CWTeam) team)
                .forEach(team -> scoreboardLocation.addText(team.getTeamText(), DisplayProperties.create(-0.115, never())));
    }
}
