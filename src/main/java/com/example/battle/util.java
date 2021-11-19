package com.example.battle;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.lang.reflect.WildcardType;

public class util {
    Battle battle;
    public util(Battle battle){
        this.battle=battle;
    }
    public void makeNewWorld(String worldName){
        if(Bukkit.getWorld(worldName)!=null){
            Bukkit.unloadWorld(worldName,true);
            deleteWorld(Bukkit.getWorld(worldName).getWorldFolder());
        }
        Bukkit.createWorld(new WorldCreator(worldName).type(WorldType.NORMAL));
    }
    public void deleteWorld(File f){
        if(f.isDirectory()){
            for(File file:f.listFiles()){
                deleteWorld(file);
            }
        }
        f.delete();
    }
}
