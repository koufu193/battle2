package com.example.battle;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.*;

public final class Battle extends JavaPlugin {
    Map<String, ChatColor> playerData=new HashMap<>();
    util util;
    int[] number=new int[2];//1:RED,2:BLUE
    Random random=new Random();
    Scoreboard scoreboard;
    boolean isStop=false;
    Location[] locations=new Location[3];//ダイヤ,鉄,金の順番
    FileConfiguration config;
    ArrayList<Player> redteam = new ArrayList<>();
    ArrayList<Player> blueteam = new ArrayList<>();
    ArrayList<Player> noteam = new ArrayList<>();
    @Override
    public void onEnable() {
        isStop=false;
        saveDefaultConfig();
        config = getConfig();
        Bukkit.getPluginManager().registerEvents(new event(this),this);
        util=new util(this);
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboard.registerNewTeam("red").setPrefix("§c");
        scoreboard.registerNewTeam("blue").setPrefix("§9");
        scoreboard.getTeam("red").setAllowFriendlyFire(false);
        scoreboard.getTeam("blue").setAllowFriendlyFire(false);
        for (String name:config.getStringList("players.red")) {
            redteam.add((Player) Bukkit.getOfflinePlayer(UUID.fromString(name)));
        }
        for (String name:config.getStringList("player.blue")) {
            blueteam.add((Player) Bukkit.getOfflinePlayer(UUID.fromString(name)));
        }
        for (Player p:Bukkit.getOnlinePlayers()) {
            if (blueteam.contains(p)) {
                scoreboard.getTeam("blue").addEntry(p.getName());
            } else if (redteam.contains(p)) {
                scoreboard.getTeam("red").addEntry(p.getName());
            }
        }
        for (Player p:Bukkit.getOnlinePlayers()) {
            p.setScoreboard(scoreboard);
        }
        CheckExile();
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

    private void CheckExile() {
        for (Player p:Bukkit.getOnlinePlayers()) {
            if (!p.getInventory().containsAtLeast(new ItemStack(Material.DIAMOND),1)){
                noteam.remove(p);
                continue;
            }
            if (!noteam.contains(p)) {
                noteam.add(p);
                scoreboard.getTeam(playerData.get(p.getName()).name()).removeEntry(p.getName());
            }
        }
        for (Player p:Bukkit.getOnlinePlayers()) {
            p.setScoreboard(scoreboard);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(this,this::CheckExile,5*20L);
    }
}
