package com.example.battle;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;

public class util {
    Battle battle;
    public util(Battle battle){
        this.battle=battle;
    }
    @Nullable
    public Location getLocationByConfig(FileConfiguration config,String... key){
        String path = "";
        ConfigurationSection data;
        for(String str:key){
            path+="."+str;
        }
        if(!config.contains(path)){
            return null;
        }
        data=config.getConfigurationSection(path);
        if(!(data.contains("x")&&data.contains("y")&& data.contains("z"))){
            return null;
        }
        return new Location(Bukkit.getWorld(getWorldName()),data.getDouble("x"),data.getDouble("y"),data.getDouble("z"));
    }
    public String getWorldName(){
        if(Bukkit.getWorld(this.battle.getConfig().getString("newWorldName"))!=null){
            return this.battle.getConfig().getString("newWorldName");
        }
        return "world";
    }
}
