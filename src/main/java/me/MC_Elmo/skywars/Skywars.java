package me.MC_Elmo.skywars;

import me.MC_Elmo.skywars.data.DataHandler;
import me.MC_Elmo.skywars.listeners.PlayerJoin;
import me.MC_Elmo.skywars.object.Game;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Elom on 4/20/17.
 */
public class Skywars extends JavaPlugin
{
    //Instances
    private Skywars instance;
    private PlayerJoin playerJoin;
    private DataHandler dataHandler;

    //Class variables
    private Set<Game> gameSet;
    private Logger logger;
    private FileConfiguration config;
    private FileConfiguration gameConfig;
    private int gamesLimit;

    public void onEnable()
    {
        this.instance = this;
        this.gameSet = new HashSet<>();
        this.logger = instance.getLogger();


        this.dataHandler = new DataHandler(instance);
        this.gameConfig = dataHandler.getGameInfo();
        setupDefaultConfig();



        playerJoin = new PlayerJoin(instance);
            getServer().getPluginManager().registerEvents(playerJoin, this);



        if(config.getBoolean("single-server-mode" , false))
        {
            gamesLimit = 1;
        }else
        {
            gamesLimit = gameConfig.getInt("max-games" , -1);
        }


        loadGames();


    }
    private void loadGames()
    {
        if(gameConfig.getConfigurationSection("games") != null)
        {
            for (String gameName : gameConfig.getConfigurationSection("games").getKeys(false))
            {
                Game game = new Game(instance, gameName);
                this.registerGame(game);
            }
        }else
        {
            getLogger().warning("There were no games created. Please create a game to use this plugin.");
        }
    }

    private void setupDefaultConfig()
    {
        this.config = getConfig();
        config.options().copyDefaults(true);
        config.options().copyHeader(true);
        saveDefaultConfig();
    }



    public Game getGame(String gameName)
    {
        for(Game game: gameSet)
        {
            if(game.getDisplayName().equalsIgnoreCase(gameName))
                return game;
        }
        return null;
    }

    public FileConfiguration getGameConfig()
    {
        return dataHandler.getGameInfo();
    }


    public Logger getLog()
    {
        return logger;
    }

    public void saveGameConfig()
    {
        boolean saved = dataHandler.saveGameInfo();
        if(saved)
            logger.info("Successfully saved gameinfo.yml");
        else
            logger.warning("Failed to save gameinfo.yml");

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

