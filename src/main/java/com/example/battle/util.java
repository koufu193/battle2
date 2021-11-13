package com.example.battle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.io.File;

public class util {
    Battle battle;
    public util(Battle battle){
        this.battle=battle;
    }
    public void makeNewWorld(){
        if(Bukkit.getWorld(this.battle.getConfig().getString("newWorldName"))!=null){
            Bukkit.unloadWorld(this.battle.getConfig().getString("newWorldName"),true);
            deleteWorld(Bukkit.getWorld(this.battle.getConfig().getString("newWorldName")).getWorldFolder());
        }
        Bukkit.createWorld(new WorldCreator(this.battle.getConfig().getString("newWorldName")).type(WorldType.NORMAL));
        String[] data={"diamond","iron","gold"};
        for(int i=0;i<3;i++){
            this.battle.locations[i]=new Location(Bukkit.getWorld(this.battle.getConfig().getString("newWorldName")),this.battle.getConfig().getInt("Locations."+data[i]+".x"),this.battle.getConfig().getInt("Locations."+data[i]+".y"),this.battle.getConfig().getInt("Locations."+data[i]+".z"));
        }
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
