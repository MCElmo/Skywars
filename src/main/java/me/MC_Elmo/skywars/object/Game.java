package me.MC_Elmo.skywars.object;

import me.MC_Elmo.skywars.Skywars;
import me.MC_Elmo.skywars.tasks.GameCountdownTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Elom on 4/20/17.
 */
public class Game
{
    //Game Info
    private String displayName;
    private int maxPlayers;
    private int minPlayers;
    private int gameStartTime;
    private World world;

    //Class variables
    private Skywars instance;
    private FileConfiguration gameConfig;

    private List<Location> spawnpoints;

    private Location lobbySpawnPoint;

    private Set<GamePlayer> players;
    private Set<GamePlayer> spectators;
    private Map<GamePlayer, Location> playerSpawnPoints;
    private boolean isTeamGame;
    private GameState gameState;
    private Logger logger;

    public Game(Skywars instance, String gameName)
    {
        playerSpawnPoints = new HashMap<>();
        this.instance = instance;
        this.logger = instance.getLog();
        gameConfig = instance.getGameConfig();
        gameState = GameState.LOBBY;
        initializeGameInfo(gameName);
        setupSpawnPoints(gameName);

        this.isTeamGame = gameConfig.getBoolean("games." + gameName + ".isTeamGame");
        this.players = new HashSet<>();
        this.spectators = new HashSet<>();
    }

    private void setupSpawnPoints(String gameName)
    {
        try
        {
            String lobbySpawnPoint = gameConfig.getString("games." + gameName + ".lobbySpawnPoint");
            String[] coords = lobbySpawnPoint.split(",");
            double x = Double.parseDouble(coords[0].split(":")[1]);
            double y = Double.parseDouble(coords[1].split(":")[1]);
            double z = Double.parseDouble(coords[2].split(":")[1]);
            float yaw = Float.parseFloat(coords[3].split(":")[1]);
            float pitch = Float.parseFloat(coords[4].split(":")[1]);
            this.lobbySpawnPoint = new Location(world,x,y,z, yaw,pitch);
            logger.info("Set lobby spawnpoint to " + x + "," + y + "," + z);
        } catch (Exception ex)
        {
            logger.severe("Failed to load Lobby spawn point with data : " + lobbySpawnPoint+ " for game : " + gameName + "." );
            logger.severe(ex.getLocalizedMessage());
        }

        spawnpoints = new ArrayList<>();
        for(String point : gameConfig.getStringList("games." + gameName +".spawnPoints"))
        {
            String[] coords = point.split(",");
            double x = Double.parseDouble(coords[0].split(":")[1]);
            double y = Double.parseDouble(coords[1].split(":")[1]);
            double z = Double.parseDouble(coords[2].split(":")[1]);
            float yaw = Float.parseFloat(coords[3].split(":")[1]);
            float pitch = Float.parseFloat(coords[4].split(":")[1]);
            Location loc = new Location(world,x,y,z, yaw,pitch);
            spawnpoints.add(loc);
            //Foramt : X:0,Y:0,Z:0,Y:0,P:0
            try {

            } catch (Exception ex) {
                logger.severe("Failed to load Spawnpoint with data : " + point + " for game : " + gameName + "." );
                logger.severe(ex.getLocalizedMessage());
            }
        }
        if(spawnpoints.size() < maxPlayers)
        {
            logger.severe("Not enough spawnpoints in game : " + gameName + " to satisfy player limit. ");
        }
    }


    private void initializeGameInfo(String gameName)
    {
        try
        {
            this.displayName = gameConfig.getString("games." + gameName + ".displayName");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            this.gameStartTime = gameConfig.getInt("games." + gameName + ".gameStartTime");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            this.maxPlayers = gameConfig.getInt("games." + gameName + ".maxPlayers");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            this.minPlayers = gameConfig.getInt("games." + gameName + ".minPlayers");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            this.world = Bukkit.createWorld(new WorldCreator(gameConfig.getString("games." + gameName + ".worldName")));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    public boolean joinGame(GamePlayer gamePlayer)
    {
        if(getPlayers().size() == maxPlayers)
        {
            gamePlayer.sendMessage(ChatColor.RED + "The game has reached its capacity!");
            return false;
        }
        if (isState(GameState.LOBBY) || isState(GameState.STARTING))
        {
            getPlayers().add(gamePlayer);
            gamePlayer.teleport(isState(GameState.LOBBY) ? lobbySpawnPoint : null/*TODO : Teleport to a spawnpoint*/);
            this.sendMessage("&a [+] &6 " + gamePlayer.getName() + "&7(&6" + getPlayers().size() + " &7/&6 " +getMaxPlayers() + "&7)");
            if(getPlayers().size() == getMinPlayers())
            {
                if(!isState(GameState.STARTING))
                    setState(GameState.STARTING);
                sendMessage(ChatColor.GOLD +  "&lThe game will begin in " + gameStartTime + " seconds");
                startCountdown(gameStartTime);
            }
            return true;
        } else
        {

            getSpectators().add(gamePlayer);
            //TODO Process spectator
            return true;
        }
    }

    private void startCountdown(int gameStartTime)
    {
        int id = 0;
        for (GamePlayer gamePlayer:
             getPlayers())
        {
            try
            {
                playerSpawnPoints.put(gamePlayer,spawnpoints.get(id));
                gamePlayer.teleport(spawnpoints.get(id));
                id++;
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        //TODO: Countdown
       new GameCountdownTask(this,gameStartTime).runTaskTimer(instance,20,20);




    }

    public void setState(GameState state)
    {
        this.gameState = state;
    }
    public boolean isState(GameState state)
    {
        return state == gameState;
    }
    public GameState getGameState()
    {
        return gameState;
    }
    public int getMaxPlayers()
    {
        return maxPlayers;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public int getMinPlayers()
    {
        return minPlayers;
    }

    public Set<GamePlayer> getPlayers()
    {
        return players;
    }

    public Set<GamePlayer> getSpectators()
    {
        return spectators;
    }

    public boolean isTeamGame()
    {
        return isTeamGame;
    }
    public void sendMessage(String message) {
        for(GamePlayer player : players)
        {
            player.sendMessage(message);
        }
    }
    public enum GameState
    {
        LOBBY, STARTING, ACTIVE, DEATHMATCH, ENDING
    }

}
