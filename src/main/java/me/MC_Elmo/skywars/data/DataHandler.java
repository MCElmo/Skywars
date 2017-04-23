package me.MC_Elmo.skywars.data;

import me.MC_Elmo.skywars.Skywars;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by Elom on 4/20/17.
 */
public class DataHandler
{
    private File gameInfoFile;
    private FileConfiguration gameInfo;
    private DataHandler dataInstance;



    public DataHandler(Skywars instance)
    {
        dataInstance = this;
        this.gameInfoFile = new File(instance.getDataFolder(), "gameInfo.yml");
        if (!this.gameInfoFile.exists()) {
            try {
                this.gameInfoFile.createNewFile();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        this.gameInfo = YamlConfiguration.loadConfiguration(this.gameInfoFile);
    }



    public FileConfiguration getGameInfo()
    {
        return gameInfo;
    }

    public boolean saveGameInfo()
    {
        try
        {
            this.gameInfo.save(this.gameInfoFile);
            return true;
        } catch (IOException e)
        {
            return false;
        }
    }
}
