package me.MC_Elmo.skywars.listeners;

import me.MC_Elmo.skywars.Skywars;
import me.MC_Elmo.skywars.object.Game;
import me.MC_Elmo.skywars.object.GamePlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Elom on 4/22/17.
 */
public class PlayerJoin implements Listener
{
    private Skywars instance;
    public PlayerJoin(Skywars instance)
    {
        this.instance = instance;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();

        Game exGame = instance.getGame("example");
        if(exGame != null)
        {
            exGame.joinGame(new GamePlayer(player));
        }else
        {
            player.sendMessage(ChatColor.RED +  " Something failed bro :/ ");
        }

    }
}
