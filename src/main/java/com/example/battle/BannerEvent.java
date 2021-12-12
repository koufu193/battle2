package com.example.battle;

import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class BannerEvent implements Listener {
    Battle battle;
    BukkitRunnable red_banner_timer;
    BukkitRunnable blue_banner_timer;
    public BannerEvent(Battle battle){
        this.battle=battle;
    }
}
