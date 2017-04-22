package me.MC_Elmo.skywars.object;

import me.MC_Elmo.skywars.data.DataHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

/**
 * Created by Elom on 4/20/17.
 */
public class Game
{
    private String displayName;


//derp
    private int maxPlayers;
    private int minPlayers;
    private int gameStartTime;
    private World world;
    private Set<Location> spawnpoints;
    private FileConfiguration config;
    private Location lobbySpawnPoint;

    private Set<GamePlayer> players;
    private Set<GamePlayer> spectators;
    private boolean isTeamGame;
    private GameState gameState;

    public Game(String gameName)
    {
        config = DataHandler.getGameInfo();
        this.displayName = config.getString("games." + gameName + ".displayName");
        gameState = GameState.LOBBY;
        this.gameStartTime = config.getInt("games." + gameName + ".gameStartTime");
        this.maxPlayers = config.getInt("games." + gameName + ".maxPlayers");
        this.minPlayers = config.getInt("games." + gameName + ".minPlayers");
        this.world = Bukkit.createWorld(new WorldCreator(config.getString("games." + gameName + ".worldName")));

       try
       {
           String lobbySpawnPoint = config.getString("games." + gameName + ".lobbySpawnPoint");
            String[] coords = lobbySpawnPoint.split(",");
            double x = Double.parseDouble(coords[0].split(":")[1]);
            double y = Double.parseDouble(coords[1].split(":")[1]);
            double z = Double.parseDouble(coords[2].split(":")[1]);
            float yaw = Float.parseFloat(coords[3].split(":")[1]);
            float pitch = Float.parseFloat(coords[4].split(":")[1]);
            this.lobbySpawnPoint = new Location(world,x,y,z, yaw,pitch);
        } catch (Exception ex)
       {
           Bukkit.getServer().getLogger().severe("Failed to load Lobby spawn point with data : " + lobbySpawnPoint+ " for game : " + gameName + "." );
           Bukkit.getServer().getLogger().severe(ex.getLocalizedMessage());
       }


        for(String point : config.getStringList("games." + gameName +".spawnPoints"))
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
                Bukkit.getServer().getLogger().severe("Failed to load Spawnpoint with data : " + point + " for game : " + gameName + "." );
                Bukkit.getServer().getLogger().severe(ex.getLocalizedMessage());
            }
        }
        this.isTeamGame = config.getBoolean("games." + gameName + ".isTeamGame");
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
            this.sendMessage("&a [+] &6 " + gamePlayer + "&7( &6" + getPlayers().size() + " &7/&6 " +getMaxPlayers() + "&7)");
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

    private void startCountdown(int countTime)
    {
        //TODO: Countdown
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
