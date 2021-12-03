package com.example.battle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Game {
    Battle battle;
    public Game(Battle battle){
        this.battle=battle;
    }
    public void startGame(){
        this.battle.isStart=true;
        for(Player p:Bukkit.getOnlinePlayers()) {
            this.battle.info.addPlayer(p);
        }
        Bukkit.getPluginManager().registerEvents(this.battle.manager,this.battle);
        new BukkitRunnable(){
            PotionEffect kishi=new PotionEffect(PotionEffectType.INCREASE_DAMAGE,90,1);
            PotionEffect sakimori=new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,90,1);
            @Override
            public void run() {
                while(battle.isStart){
                    for(String str:battle.kishi_sakimori_data.keySet()){
                        battle.kishi_sakimori_data.get(str).forEach(b->Bukkit.getPlayer(b).addPotionEffect(str.equals(battle.KISHI_AKASHI_NAME)?kishi:sakimori));
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTask(this.battle);
    }
    public void finishGame(){//後処理(プレーヤーのtpとか)
        HandlerList.unregisterAll(this.battle);
        Location location=this.battle.util.getLocationByConfig(this.battle.getConfig(),"Locations","finishedTeleportLocation");
        this.battle.info.playerColor.clear();
        for(String str:this.battle.kishi_sakimori_data.keySet()){
            this.battle.kishi_sakimori_data.get(str).clear();
        }
        this.battle.isStart=false;
        if(location==null){
            this.battle.getLogger().warning("テレポート先が見つかりません");
            throw new IllegalArgumentException("configの項目が足りないです<-Locations.finishedTeleportLocation");
        }else {
            location.setWorld(Bukkit.getWorld(this.battle.getConfig().getString("FinishedTeleportWorld")));
            this.battle.info.playerColor.keySet().forEach(b -> Bukkit.getPlayer(b).teleport(location));
            this.battle.canStart.set(false);
            Bukkit.getScheduler().runTaskLater(this.battle,bukkitTask -> battle.canStart.set(true),this.battle.getConfig().getInt("waitTime")*20);
            this.battle.util.makeNewWorld(this.battle.getConfig().getString("newWorldName"));
        }
    }
}
