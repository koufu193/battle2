package com.example.battle;

import org.bukkit.ChatColor;

public enum PlayerType {
    BOUMEI_BLUE,BOUMEI_RED,BLUE,RED;
    public ChatColor getColor(){
        if(this==BOUMEI_BLUE||this==BOUMEI_RED){
            return ChatColor.WHITE;
        }
        return this==BLUE?ChatColor.DARK_BLUE:ChatColor.DARK_RED;
    }
    public PlayerType getBeenColor(){
        if(this==BOUMEI_BLUE){
            return BLUE;
        }else if(this==BOUMEI_RED){
            return RED;
        }
        return this;
    }
    public PlayerType getChangeColor(){
        if(this==BLUE){
            return RED;
        }else if(this==RED){
            return BLUE;
        }
        return this;
    }
    public PlayerType getBoumei(){
        if(this==BLUE){
            return BOUMEI_BLUE;
        }else if(this==RED){
            return BOUMEI_RED;
        }
        return this;
    }
    public boolean isBoumei(){
        return this==BOUMEI_BLUE||this==BOUMEI_RED;
    }
}
