package me.MC_Elmo.skywars;

import me.MC_Elmo.skywars.object.Game;
import me.MC_Elmo.skywars.data.DataHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Elom on 4/20/17.
 */
public class Skywars extends JavaPlugin
{
    private Skywars instance;
    private DataHandler dataHandler;
    private Set<Game> gameSet = new HashSet<>();
    private FileConfiguration config;
    private int gamesLimit = 0;

    public void onEnable()
    {
        this.config = getConfig();
        this.instance = this;
        this.dataHandler = new DataHandler(instance);
        config.options().copyDefaults(true);
        config.options().copyHeader(true);
        saveDefaultConfig();

        if(getConfig().getBoolean("single-server-mode"))
        {
            gamesLimit = 1;
        }else
        {
            gamesLimit = DataHandler.getGameInfo().getInt("max-games" , -1);
        }
        if(dataHandler.getGameInfo().getConfigurationSection("games") != null)
        {
            for (String gameName : dataHandler.getGameInfo().getConfigurationSection("games").getKeys(false))
            {
                Game game = new Game(gameName);
                this.registerGame(game);
            }
        }else
        {
            getLogger().warning("There were no games created. Please create a game to use this plugin.");
        }
    }

    @Override
    public void onDisable()
    {
        instance = null;
    }

    public boolean registerGame(Game game) {
        if(gameSet.size() == gamesLimit && gamesLimit != -1) //If there is a capacity and it has been met
        {
            return false;
        }

        gameSet.add(game);

        return true;
    }

}

/*
- Bungee Support AND one server support
 */
