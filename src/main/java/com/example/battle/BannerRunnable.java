package com.example.battle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BannerRunnable implements Runnable {
    String title;
    ChatColor color;
    int now;
    int max;
    Battle battle;
    public BannerRunnable(String title,int now,int max,ChatColor color,Battle battle){
        this.title=title;
        this.max=max;
        this.now=now;
        this.color=color;
        this.battle=battle;
    }
    @Override
    public void run() {
        for(Player p: Bukkit.getOnlinePlayers()){
            if(now==0) {
                Bukkit.broadcastMessage(color+title+"群が制圧旗を置いた");
                p.sendTitle(color + "=="+title+"群が制圧旗を掲げた==", color + "制圧まであと" + (max - now) + "時間",10,10,10);
            }else if(now+1!=max) {
                p.sendTitle(color + title + "チーム、制圧まであと" + (max - now) + "時間","", 10, 10, 10);
            }else{
                p.sendTitle(color+title+"チームの勝利!","",10,10,10);
            }
        }
        if(now==max) {
            this.battle.game.finishGame();
        }
        now++;
    }
}
