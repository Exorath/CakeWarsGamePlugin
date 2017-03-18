package com.exorath.plugin.game.cakewars.startTeleport;

import com.exorath.exoteams.Team;
import com.exorath.exoteams.TeamAPI;
import com.exorath.plugin.basegame.manager.ListeningManager;
import com.exorath.plugin.basegame.state.State;
import com.exorath.plugin.basegame.state.StateChangeEvent;
import com.exorath.plugin.basegame.team.TeamManager;
import com.exorath.plugin.game.cakewars.team.CWTeam;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;

/**
 * Created by toonsev on 3/18/2017.
 */
public class StartTeleportManager implements ListeningManager {
    private TeamAPI teamAPI;
    public StartTeleportManager(TeamAPI teamAPI) {
        this.teamAPI = teamAPI;
    }

    @EventHandler
    public void onStateChange(StateChangeEvent event){
        if(event.getNewState() == State.STARTED){
            for(Team team : teamAPI.getTeams()){
                if(team instanceof CWTeam){
                    CWTeam cwTeam = (CWTeam) team;
                    Location spawn = cwTeam.getSpawnLocation();
                    cwTeam.getPlayers().forEach(player -> TeamManager.getPlayer(player).teleport(spawn));
                }
            }
        }
    }
}
