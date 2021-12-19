package com.example.battle;

import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class Game {
    Battle battle;
    public Game(Battle battle){
        this.battle=battle;
    }
    public void startGame(){
        this.battle.isStart.set(true);
        for(Player p:Bukkit.getOnlinePlayers()) {
            this.battle.info.addPlayer(p);
        }
        Bukkit.getPluginManager().registerEvents(this.battle.manager,this.battle);
        Bukkit.getPluginManager().registerEvents(this.battle.event,this.battle);
        this.battle.blue_spawn_location.getBlock().setType(Material.END_PORTAL);
        this.battle.red_spawn_location.getBlock().setType(Material.END_PORTAL);
        Bukkit.getScheduler().runTaskTimer(this.battle,new Runnable(){
            PotionEffect kishi=new PotionEffect(PotionEffectType.INCREASE_DAMAGE,90*20,1);
            PotionEffect sakimori=new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,90*20,1);
            @Override
            public void run() {
                for (String str : battle.kishi_sakimori_data.keySet()) {
                    battle.kishi_sakimori_data.get(str).forEach(b -> Bukkit.getPlayer(b).addPotionEffect(str.equals(battle.KISHI_AKASHI_NAME) ? kishi : sakimori));
                }
            }
        },0,60*20);
    }
    public void finishGame(){//後処理(プレーヤーのtpとか)
        Bukkit.getScheduler().cancelTasks(this.battle);
        HandlerList.unregisterAll(this.battle);
        Location location=this.battle.util.getLocationByConfig(this.battle.getConfig(),"Locations","finishedTeleportLocation");
        this.battle.info.playerColor.clear();
        for(String str:this.battle.kishi_sakimori_data.keySet()){
            this.battle.kishi_sakimori_data.get(str).clear();
        }
        this.battle.isStart.set(false);
        new File(this.battle.getDataFolder(),"banner.txt").delete();
        new File(this.battle.getDataFolder(),"save.txt").delete();
        if(location==null){
            this.battle.getLogger().warning("テレポート先が見つかりません");
            throw new IllegalArgumentException("configの項目が足りないです<-Locations.finishedTeleportLocation");
        }else {
            location.setWorld(Bukkit.getWorld(this.battle.getConfig().getString("FinishedTeleportWorld")));
            this.battle.info.playerColor.keySet().forEach(b -> Bukkit.getPlayer(b).teleport(location));
            this.battle.canStart.set(false);
            this.battle.util.makeNewWorld(this.battle.getConfig().getString("newWorldName"));
        }
    }
}
