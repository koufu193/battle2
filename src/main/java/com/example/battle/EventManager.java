package com.example.battle;

import org.bukkit.event.Listener;

public class EventManager implements Listener {
    Battle battle;
    public EventManager(Battle battle){
        this.battle=battle;
    }
}
