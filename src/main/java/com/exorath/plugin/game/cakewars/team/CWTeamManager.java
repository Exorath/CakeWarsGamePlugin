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

package com.exorath.plugin.game.cakewars.team;

import com.exorath.exoteams.Team;
import com.exorath.exoteams.TeamAPI;
import com.exorath.exoteams.player.TeamPlayer;
import com.exorath.plugin.basegame.BaseGameAPI;
import com.exorath.plugin.basegame.lib.LocationSerialization;
import com.exorath.plugin.basegame.manager.ListeningManager;
import com.exorath.plugin.basegame.team.TeamManager;
import com.exorath.plugin.game.cakewars.Main;
import com.exorath.plugin.game.cakewars.players.PlayerManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by toonsev on 3/15/2017.
 */
public class CWTeamManager implements ListeningManager {
    private TeamAPI teamAPI;

    public CWTeamManager(TeamAPI teamAPI, ConfigurationSection teamsSection) {
        this.teamAPI = teamAPI;
        loadTeams(teamsSection);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Team team = teamAPI.onPlayerJoin(TeamManager.getTeamPlayer(event.getPlayer().getUniqueId().toString()));
        if(team != null)
            BaseGameAPI.getInstance().getManager(PlayerManager.class);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        teamAPI.onPlayerLeave(TeamManager.getTeamPlayer(event.getPlayer().getUniqueId().toString()));
    }
    private void loadTeams(ConfigurationSection teamsSection) {
        if (teamsSection == null)
            Main.terminate("No teams configuration");
        for (String key : teamsSection.getKeys(false))
            loadTeam(teamsSection.getConfigurationSection(key));
    }

    private void loadTeam(ConfigurationSection teamSection) {
        if (!teamSection.contains("name"))
            Main.terminate("No name in team map section");
        if (!teamSection.contains("spawnLocation"))
            Main.terminate("No spawnLocation in team map section");
        if (!teamSection.contains("cakeLocation"))
            Main.terminate("No cakeLocation in team map section");
        if (!teamSection.contains("primaryShopLocation"))
            Main.terminate("No primaryShopLocation in team map section");
        World world = Main.getInstance().getMapsManager().getGameMap().getWorld();
        int maxPlayers = teamSection.contains("maxPlayers") ? teamSection.getInt("maxPlayers") : 0;
        teamAPI.addTeam(new CWTeam(
                ChatColor.translateAlternateColorCodes('&',teamSection.getString("name")),
                LocationSerialization.getLocation(world, teamSection.getConfigurationSection("cakeLocation")),
                LocationSerialization.getLocation(world, teamSection.getConfigurationSection("spawnLocation")),
                LocationSerialization.getLocation(world, teamSection.getConfigurationSection("primaryShopLocation")),
                maxPlayers));
    }
}
