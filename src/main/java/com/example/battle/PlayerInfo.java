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

public class PlayerInfo {
    final Battle battle;
    private final Map<String, ChatColor> playerColor = new HashMap<>();
    boolean isRed = true;

    public PlayerInfo(Battle battle) {
        this.battle = battle;
    }

    @Nullable
    public ChatColor getColorByPlayerName(String playerName) {
        return playerColor.getOrDefault(playerName, null);
    }

    public void addPlayer(org.bukkit.entity.Player player) {
        if (playerColor.containsKey(player.getName())) {
            changePlayer(player);
        } else {
            ChatColor color = getColor();
            playerColor.put(player.getName(), color);
            player.setScoreboard(this.battle.scoreboard);
            this.battle.scoreboard.getTeam(color==ChatColor.DARK_BLUE?"blue_team":"red_team").addEntry(player.getName());
            player.setPlayerListName(color+player.getPlayerListName());
        }
    }

    public void changePlayer(org.bukkit.entity.Player player) {
        if (!playerColor.containsKey(player.getName())) {
            addPlayer(player);
        } else {
            ChatColor color = getColor();
            playerColor.put(player.getName(), color);
            this.battle.scoreboard.getTeam(color.name()).addEntry(player.getName());
            this.battle.scoreboard.getTeam(color==ChatColor.DARK_BLUE?"blue_team":"red_team").removeEntry(player.getName());
            player.setPlayerListName(color+player.getPlayerListName().replaceAll("§.",""));
        }
    }
    //ポーションを上げれたらtrue、違うチームとかであげれなかったらfalse
    public boolean addEffect(Player p,String itemName){
        if(playerColor.containsKey(p.getName())){
            String color=playerColor.get(p.getName()).name();
            if(itemName.matches("§"+color+"[("+ battle.SAKIMORI_AKASHI_NAME+")("+ battle.KISHI_AKASHI_NAME+")]")){
                boolean isSakimori=itemName.matches("§"+color+ battle.SAKIMORI_AKASHI_NAME);
                if(!this.battle.kishi_sakimori_data.get(isSakimori?battle.KISHI_AKASHI_NAME:battle.SAKIMORI_AKASHI_NAME).contains(p.getName())) {
                    PotionEffectType type = isSakimori ? PotionEffectType.DAMAGE_RESISTANCE : PotionEffectType.INCREASE_DAMAGE;
                    p.addPotionEffect(new PotionEffect(type, 1, 1));
                    this.battle.kishi_sakimori_data.get(isSakimori? battle.SAKIMORI_AKASHI_NAME : battle.KISHI_AKASHI_NAME).add(p.getName());
                    return true;
                }else{
                    p.removePotionEffect(isSakimori?PotionEffectType.INCREASE_DAMAGE:PotionEffectType.DAMAGE_RESISTANCE);
                    Arrays.asList(p.getInventory().getContents()).removeIf(b->b.hasItemMeta()?b.getItemMeta().getDisplayName().matches("§.[("+this.battle.SAKIMORI_AKASHI_NAME+")("+ battle.KISHI_AKASHI_NAME+")]"):false);
                }
            }
        }
        return false;
    }
    private ChatColor getColor() {
        isRed = !isRed;
        return isRed ? ChatColor.DARK_RED : ChatColor.DARK_BLUE;
    }
}