package com.example.battle;

public class DiscordSRV {
    Battle battle;
    public DiscordSRV(Battle battle){
        this.battle=battle;
    }
    public void showText(String text){
        text=text.replaceAll("§.","");
    }
}
