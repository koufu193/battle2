package com.example.battle;

import org.bukkit.ChatColor;

import javax.annotation.Nullable;
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

    private ChatColor getColor() {
        isRed = !isRed;
        return isRed ? ChatColor.DARK_RED : ChatColor.DARK_BLUE;
    }
}