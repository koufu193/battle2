package com.example.battle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.minidns.record.A;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BannerRunnable implements Runnable {
    String title;
    ChatColor color;
    AtomicInteger now;
    AtomicInteger max;
    Battle battle;
    public BannerRunnable(String title,int now,int max,ChatColor color,Battle battle){
        this.title=title;
        this.max=new AtomicInteger(max);
        this.now=new AtomicInteger(now);
        this.color=color;
        this.battle=battle;
    }
    @Override
    public void run() {
        for(Player p: Bukkit.getOnlinePlayers()){
            if(now.get()==0) {
                Bukkit.broadcastMessage(color+title+"群が制圧旗を置いた");
                this.battle.srv.showText(title+"群が制圧旗を置いた", Color.RED);
                p.sendTitle(color + "=="+title+"群が制圧旗を掲げた==", color + "制圧まであと" + (max.get() - now.get()) + "時間",10,10,10);
            }else if(now.get()+1!=max.get()) {
                p.sendTitle(color + title + "チーム、制圧まであと" + (max.get() - now.get()) + "時間","", 10, 10, 10);
                this.battle.srv.showText(title+"チーム、制圧まであと"+(max.get()-now.get())+"時間",Color.GREEN);
            }else{
                p.sendTitle(color+title+"チームの勝利!","",10,10,10);
                this.battle.srv.showText(title+"チームの勝利!",Color.YELLOW);
            }
        }
        if(now.get()==max.get()) {
            this.battle.game.finishGame();
        }
        now.set(now.get()+1);
    }
}
