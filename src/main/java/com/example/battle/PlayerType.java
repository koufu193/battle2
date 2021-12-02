package com.example.battle;

import org.bukkit.ChatColor;

public enum PlayerType {
    BOUMEI_BLUE,BOUMEI_RED,BLUE,RED;
    public ChatColor getColor(PlayerType type){
        if(type==BOUMEI_BLUE||type==BOUMEI_RED){
            return ChatColor.WHITE;
        }
        return type==BLUE?ChatColor.BLUE:ChatColor.RED;
    }
    public PlayerType getBeenColor(PlayerType type){
        if(type==BOUMEI_BLUE){
            return BLUE;
        }else if(type==BOUMEI_RED){
            return RED;
        }
        return type;
    }
}
