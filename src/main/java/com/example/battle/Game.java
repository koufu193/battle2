package com.example.battle;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Game {
    Battle battle;
    public Game(Battle battle){
        this.battle=battle;
    }
    public void startGame(){

    }
    public void finishGame(){//後処理(プレーヤーのtpとか)
        Location location=this.battle.util.getLocationByConfig(this.battle.getConfig(),"Locations","finishedTeleportLocation");
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
        this.battle.info.playerColor.clear();
        for(String str:this.battle.kishi_sakimori_data.keySet()){
            this.battle.kishi_sakimori_data.get(str).clear();
        }
    }
}
