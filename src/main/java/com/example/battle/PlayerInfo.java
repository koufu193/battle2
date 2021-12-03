package com.example.battle;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
            player.setScoreboard(this.battle.scoreboard);
            this.battle.scoreboard.getTeam(type.getColor()==ChatColor.DARK_BLUE?"blue_team":"red_team").addEntry(player.getName());
            player.setPlayerListName(type.getColor()+player.getPlayerListName());
        }
    }
    public void boumeiPlayer(Player player){
        if(!playerColor.containsKey(player.getUniqueId())){
            addPlayer(player);
        }else if(playerColor.get(player.getUniqueId()).isBoumei()){
            changePlayer(player);
        }
    }
    public void changePlayer(Player player) {
        if (!playerColor.containsKey(player.getUniqueId())) {
            addPlayer(player);
        }else if(!playerColor.get(player.getUniqueId()).isBoumei()){
            boumeiPlayer(player);
        }else{
            PlayerType type=playerColor.get(player.getUniqueId()).getBeenColor().getChangeColor();
            playerColor.put(player.getUniqueId(), type);
            this.battle.scoreboard.getTeam(type.getColor()==ChatColor.DARK_BLUE?"red_team":"blue_team").addEntry(player.getName());
            this.battle.scoreboard.getTeam(type.getColor()==ChatColor.DARK_BLUE?"blue_team":"red_team").removeEntry(player.getName());
            player.setPlayerListName(type.getColor()+player.getPlayerListName().replaceAll("§.",""));
        }
    }
    //ポーションを上げれたらtrue、違うチームとかであげれなかったらfalse
    public boolean addEffect(Player p,String itemName){
        if(playerColor.containsKey(p.getUniqueId())){
            String color=playerColor.get(p.getUniqueId()).getColor().name();
            if(itemName.matches("§"+color+"[("+ battle.SAKIMORI_AKASHI_NAME+")("+ battle.KISHI_AKASHI_NAME+")]")){
                boolean isSakimori=itemName.matches("§"+color+ battle.SAKIMORI_AKASHI_NAME);
                if(Arrays.stream(p.getInventory().getContents()).anyMatch(b->b.getItemMeta().getDisplayName().equals(itemName))){
                    return false;
                }
                if(!this.battle.kishi_sakimori_data.get(isSakimori?battle.KISHI_AKASHI_NAME:battle.SAKIMORI_AKASHI_NAME).contains(p.getName())) {
                    PotionEffectType type = isSakimori ? PotionEffectType.DAMAGE_RESISTANCE : PotionEffectType.INCREASE_DAMAGE;
                    p.addPotionEffect(new PotionEffect(type, 90, 1));
                    this.battle.kishi_sakimori_data.get(isSakimori? battle.SAKIMORI_AKASHI_NAME : battle.KISHI_AKASHI_NAME).add(p.getUniqueId());
                    return true;
                }else{
                    p.removePotionEffect(isSakimori?PotionEffectType.INCREASE_DAMAGE:PotionEffectType.DAMAGE_RESISTANCE);
                    Arrays.asList(p.getInventory().getContents()).removeIf(b->b.hasItemMeta()?b.getItemMeta().getDisplayName().matches("§.[("+this.battle.SAKIMORI_AKASHI_NAME+")("+ battle.KISHI_AKASHI_NAME+")]"):false);
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