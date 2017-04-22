package me.MC_Elmo.skywars.utility;

import org.bukkit.ChatColor;

/**
 * Created by Elom on 4/21/17.
 */
public class ChatUtil
{
    public static  String format(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
