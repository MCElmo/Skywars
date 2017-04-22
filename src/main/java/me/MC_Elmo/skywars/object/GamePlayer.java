package me.MC_Elmo.skywars.object;

import me.MC_Elmo.skywars.utility.ChatUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Elom on 4/21/17.
 */
public class GamePlayer
{
    private Player player;
    private String name;

    public GamePlayer(Player player)
    {
        this.player = player;
        this.name = player.getName();
    }

    public String getName()
    {
        return name;
    }
    public void sendMessage(String message)
    {
        player.sendMessage(ChatUtil.format(message));
    }
    public void teleport(Location location)
    {
        player.teleport(location);
    }

    public enum GamePlayerState {

    }

}

