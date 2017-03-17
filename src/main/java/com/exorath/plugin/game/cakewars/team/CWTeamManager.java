package com.exorath.plugin.game.cakewars.team;

import com.exorath.exoteams.TeamAPI;
import com.exorath.exoteams.player.TeamPlayer;
import com.exorath.plugin.basegame.manager.ListeningManager;
import com.exorath.plugin.basegame.team.TeamManager;
import com.exorath.plugin.game.cakewars.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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
        teamAPI.onPlayerJoin(TeamManager.getTeamPlayer(event.getPlayer().getUniqueId().toString()));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        teamAPI.onPlayerLeave(TeamManager.getTeamPlayer(event.getPlayer().getUniqueId().toString()));
    }
    private void loadTeams(ConfigurationSection teamsSection) {
        if (teamsSection == null) {
            System.out.println("No teams configuration");
            Main.terminate();
        }
        for (String key : teamsSection.getKeys(false))
            loadTeam(teamsSection.getConfigurationSection(key));
    }

    private void loadTeam(ConfigurationSection teamSection) {
        if (!teamSection.contains("spawnLocation")) {
            System.out.println("No spawnLocation in team map section");
            Main.terminate();
        }
        if (!teamSection.contains("cakeLocation")) {
            System.out.println("No cakeLocation in team map section");
            Main.terminate();
        }
        if (!teamSection.contains("primaryShopLocation")) {
            System.out.println("No primaryShopLocation in team map section");
            Main.terminate();
        }
        World world = Main.getInstance().getMapsManager().getGameMap().getWorld();
        int maxPlayers = teamSection.contains("maxPlayers") ? teamSection.getInt("maxPlayers") : 0;
        teamAPI.addTeam(new CWTeam(
                Main.getLocation(world, teamSection.getConfigurationSection("cakeLocation")),
                Main.getLocation(world, teamSection.getConfigurationSection("spawnLocation")),
                Main.getLocation(world, teamSection.getConfigurationSection("primaryShopLocation")),
                maxPlayers));
    }
}
