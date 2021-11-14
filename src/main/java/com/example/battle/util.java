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
    public void addPlayer(Player p, ChatColor color){
        p.setBedSpawnLocation(color==ChatColor.BLUE?battle.blueSpawn:battle.redSpawn);
        p.setScoreboard(battle.scoreboard);
        this.battle.playerData.put(p.getUniqueId().toString(),color);
        p.setPlayerListName(color+p.getPlayerListName()+ChatColor.RESET);
        p.setDisplayName(color+p.getDisplayName()+ChatColor.RESET);
        this.battle.scoreboard.getTeam(color==ChatColor.RED?"red":"blue").addPlayer(p);
        if (color==ChatColor.RED) {
            battle.redteam.add(p);
        } else if (color==ChatColor.BLUE){
            battle.blueteam.add(p);
        }
    }
    public Location getlocationbyconfig(FileConfiguration config, String path, World world) {
        double x = config.getDouble(path+".x");
        double y = config.getDouble(path+".y");
        double z = config.getDouble(path+".z");
        return new Location(world,x,y,z);
    }
}
