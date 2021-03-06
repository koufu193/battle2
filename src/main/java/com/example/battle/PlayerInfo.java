package com.example.battle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;

public class PlayerInfo {
    final Battle battle;
    final Map<UUID, PlayerType> playerColor = new HashMap<>();
    boolean isRed = true;

    public PlayerInfo(Battle battle) {
        this.battle = battle;
    }

    @Nullable
    public PlayerType getColorByPlayerName(UUID uuid) {
        return playerColor.getOrDefault(uuid, null);
    }

    public void addPlayer(Player player) {
        if (playerColor.containsKey(player.getUniqueId())) {
            changePlayer(player);
        } else {
            PlayerType type=getColor();
            playerColor.put(player.getUniqueId(), type);
            this.battle.scoreboard.getTeam(type.getColor()==ChatColor.BLUE?"blue_team":"red_team").addEntry(player.getName());
            player.setPlayerListName(type.getColor()+player.getPlayerListName());
            player.teleport(type==PlayerType.BLUE?this.battle.blue_spawn_location:this.battle.red_spawn_location);
            for(Player p:Bukkit.getOnlinePlayers()){
                p.setScoreboard(this.battle.scoreboard);
            }
            this.battle.srv.changeRoll(player.getUniqueId());
        }
    }
    public void boumeiPlayer(Player player){
        if(!playerColor.containsKey(player.getUniqueId())){
            addPlayer(player);
        }else if(playerColor.get(player.getUniqueId()).isBoumei()){
            changePlayer(player);
        }else{
            player.setPlayerListName(player.getName());
            PlayerType type=playerColor.get(player.getUniqueId());
            this.battle.scoreboard.getTeam(type.getColor()==ChatColor.BLUE?"blue_team":"red_team").removeEntry(player.getName());
            this.battle.scoreboard.getTeam("boumei_team").addEntry(player.getName());
            for(Player p:Bukkit.getOnlinePlayers()){
                p.setScoreboard(this.battle.scoreboard);
            }
            playerColor.put(player.getUniqueId(),type.getBoumei());
            Bukkit.broadcastMessage(player.getName()+ChatColor.RED+"?????????????????????("+(type==PlayerType.BLUE?"???????????????":"???????????????")+"?????????????????????????????????)");
            this.battle.srv.showText(player.getName()+"?????????????????????("+(type==PlayerType.BLUE?"???????????????":"???????????????")+"?????????????????????????????????)", Color.RED);
            for(String str:this.battle.kishi_sakimori_data.keySet()){
                if(this.battle.kishi_sakimori_data.get(str).contains(player.getUniqueId())){
                    this.battle.kishi_sakimori_data.get(str).remove(player.getUniqueId());
                }
            }
            for(ItemStack item:player.getInventory().getContents()){
                if(item!=null){
                    if(item.hasItemMeta()){
                        if(item.getItemMeta().getDisplayName().matches("??."+this.battle.KISHI_AKASHI_NAME)||item.getItemMeta().getDisplayName().matches("??."+this.battle.SAKIMORI_AKASHI_NAME)||item.getItemMeta().getDisplayName().equals(this.battle.event.RED_BANNER)||item.getItemMeta().getDisplayName().equals(this.battle.event.BLUE_BANNER)){
                            player.getInventory().remove(item);
                        }
                    }
                }
            }
        }
    }
    public void changePlayer(Player player) {
        if (!playerColor.containsKey(player.getUniqueId())) {
            addPlayer(player);
        }else if(!playerColor.get(player.getUniqueId()).isBoumei()){
            boumeiPlayer(player);
        }else{
            Bukkit.broadcastMessage(player.getName()+ChatColor.GREEN+"????????????????????????!");
            this.battle.srv.showText(player.getName()+"????????????????????????!",Color.GREEN);
            PlayerType type=playerColor.get(player.getUniqueId()).getBeenColor().getChangeColor();
            playerColor.put(player.getUniqueId(), type);
            this.battle.scoreboard.getTeam("boumei_team").removeEntry(player.getName());
            this.battle.scoreboard.getTeam(type.getColor()==ChatColor.BLUE?"blue_team":"red_team").addEntry(player.getName());
            for(Player p:Bukkit.getOnlinePlayers()){
                p.setScoreboard(this.battle.scoreboard);
            }
            Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).filter(ItemStack::hasItemMeta).filter(item->item.getItemMeta().getDisplayName().equals(ChatColor.RED+"??????????????????????????????")||item.getItemMeta().getDisplayName().equals(ChatColor.BLUE+"??????????????????????????????")).forEach(b->player.getInventory().remove(b));
            player.setPlayerListName(type.getColor()+player.getPlayerListName());
            this.battle.srv.changeRoll(player.getUniqueId());
        }
    }
    public void backColor(Player player,boolean isKilled){
        if(!playerColor.containsKey(player.getUniqueId())){
            addPlayer(player);
        }else if(!playerColor.get(player.getUniqueId()).isBoumei()){
            boumeiPlayer(player);
        }else{
            PlayerType type=playerColor.get(player.getUniqueId());
            player.getInventory().clear();
            Bukkit.broadcastMessage(player.getName()+ChatColor.RED+"????????????????????????");
            this.battle.srv.showText(player.getName()+"????????????????????????",Color.RED);
            playerColor.put(player.getUniqueId(),type.getBeenColor());
            this.battle.scoreboard.getTeam("boumei_team").removeEntry(player.getName());
            this.battle.scoreboard.getTeam(type.getBeenColor()==PlayerType.BLUE?"blue_team":"red_team").addEntry(player.getName());
            player.setPlayerListName(type.getBeenColor().getColor()+player.getPlayerListName());
            if(!isKilled){
                player.teleport(type.getBeenColor()==PlayerType.BLUE?this.battle.blue_spawn_location:this.battle.red_spawn_location);
            }
            for(Player p:Bukkit.getOnlinePlayers()){
                p.setScoreboard(this.battle.scoreboard);
            }
        }
    }
    //?????????????????????????????????true???????????????????????????????????????????????????false
    public boolean addEffect(Player p,String itemName){
        if(playerColor.containsKey(p.getUniqueId())){
            ChatColor color=playerColor.get(p.getUniqueId()).getColor();
            if((itemName.equals(color+this.battle.SAKIMORI_AKASHI_NAME)||itemName.equals(color+this.battle.KISHI_AKASHI_NAME))&&!playerColor.get(p.getUniqueId()).isBoumei()){
                boolean isSakimori=itemName.matches(color+ battle.SAKIMORI_AKASHI_NAME);
                if(!this.battle.kishi_sakimori_data.get(isSakimori?battle.KISHI_AKASHI_NAME:battle.SAKIMORI_AKASHI_NAME).contains(p.getUniqueId())) {
                    if(!this.battle.kishi_sakimori_data.get(isSakimori?battle.SAKIMORI_AKASHI_NAME:battle.KISHI_AKASHI_NAME).contains(p.getUniqueId())) {
                        PotionEffectType type = isSakimori ? PotionEffectType.DAMAGE_RESISTANCE : PotionEffectType.INCREASE_DAMAGE;
                        p.addPotionEffect(new PotionEffect(type, 90 * 20, 1));
                        this.battle.kishi_sakimori_data.get(isSakimori ? battle.SAKIMORI_AKASHI_NAME : battle.KISHI_AKASHI_NAME).add(p.getUniqueId());
                        return true;
                    }
                }else{
                    p.removePotionEffect(isSakimori?PotionEffectType.INCREASE_DAMAGE:PotionEffectType.DAMAGE_RESISTANCE);
                    Arrays.stream(p.getInventory().getContents()).filter(b->b!=null).filter(b-> b.hasItemMeta() && (b.getItemMeta().getDisplayName().matches("??." + this.battle.SAKIMORI_AKASHI_NAME) || b.getItemMeta().getDisplayName().matches("??." + battle.KISHI_AKASHI_NAME))).forEach(b->p.getInventory().remove(b));
                    for(String str:this.battle.kishi_sakimori_data.keySet()){
                        this.battle.kishi_sakimori_data.get(str).remove(p.getUniqueId());
                    }
                }
            }
        }
        return false;
    }
    private PlayerType getColor() {
        isRed = !isRed;
        return isRed ? PlayerType.RED : PlayerType.BLUE;
    }
}