package me.MC_Elmo.skywars.object;

import org.bukkit.Location;

/**
 * Created by Elom on 4/21/17.
 */
public class GameTeam
{
    private String teamName;

    public GameTeam(String teamName)
    {
        this.teamName = teamName;
    }
    private GameTeam.GameTeamState gameTeamState;

    public void sendMessage(String message)
    {
        //TODO
    }

    public void teleport(Location location)
    {
       //TODO
    }

    public String getName()
    {
        return teamName;
    }
    public enum GameTeamState {

    }
}
