package me.MC_Elmo.skywars.tasks;

import me.MC_Elmo.skywars.object.Game;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Elom on 4/22/17.
 */
public class GameCountdownTask extends BukkitRunnable
{
    private Game game;
    private int time;
    public GameCountdownTask(Game game, int time)
        {
            this.game = game;
            this.time = time;
        }

    @Override
    public void run()
    {
        time = time -1;
        if(time == 0)
        {
            game.sendMessage(ChatColor.GOLD + "&l The game has started!");
            cancel();
        }
        if(time % 10 == 0)
            game.sendMessage(ChatColor.GOLD + "&l The game is starting in " + time + "seconds");
        if(time <= 5)
            game.sendMessage("The game is starting in " + time + " seconds");

    }
}
