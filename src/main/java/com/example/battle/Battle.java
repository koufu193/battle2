package com.example.battle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class Battle extends JavaPlugin {
    Map<String, ChatColor> playerData=new HashMap<>();
    util util;
    int[] number=new int[2];//1:RED,2:BLUE
    Random random=new Random();
    Scoreboard scoreboard=Bukkit.getScoreboardManager().getNewScoreboard();
    boolean isStop=false;
    Location[] locations=new Location[3];//ダイヤ,鉄,金の順番
    @Override
    public void onEnable() {
        isStop=false;
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new event(this),this);
        util=new util(this);
        scoreboard.registerNewTeam("red").setPrefix("§c");
        scoreboard.registerNewTeam("blue").setPrefix("§9");
        new BukkitRunnable(){
            @Override
            public void run() {
                int diamond=getConfig().getInt("diamond");
                int iron=getConfig().getInt("iron");
                int gold=getConfig().getInt("gold");
                int[] data=new int[3];
                while(!isStop){
                    for(int number:data){
                        number++;
                    }
                    if(diamond<=data[0]){
                        data[0]=0;

                    }
                    if(iron<=data[1]){
                        data[1]=0;
                    }
                    if(gold<=data[2]){
                        data[2]=0;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTask(this);
    }
    @Override
    public void onDisable() {
        isStop=true;
    }
}
