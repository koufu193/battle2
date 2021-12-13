package com.example.battle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BannerEvent implements Listener {
    Battle battle;
    BannerRunnable red_banner_timer;
    BannerRunnable blue_banner_timer;
    BukkitTask red;
    BukkitTask blue;
    final String RED_BANNER=ChatColor.DARK_RED+"アルティオ制圧旗";
    final String BLUE_BANNER=ChatColor.DARK_BLUE+"アプサラス制圧旗";
    Location RedBanner=null;
    Location BlueBanner=null;
    int canXZr;
    int canYr;
    int cantXZr;
    int cantYr;
    public BannerEvent(Battle battle){
        this.battle=battle;
        ConfigurationSection config=battle.getConfig().getConfigurationSection("banner");
        canXZr=config.getInt("canPlace.xzr");
        canYr=config.getInt("canPlace.yr");
        cantXZr=config.getInt("cantPlace.xzr");
        cantYr=config.getInt("cantPlace.yr");
    }
    @EventHandler
    public void placeEvent(BlockPlaceEvent e){
        if(e.getItemInHand().getItemMeta().getDisplayName().equals(RED_BANNER)||e.getItemInHand().getItemMeta().getDisplayName().equals(BLUE_BANNER)&&e.canBuild()&&!e.isCancelled()){
            PlayerType type=this.battle.info.getColorByPlayerName(e.getPlayer().getUniqueId());
            if(type!=null&&!type.isBoumei()){
                if(!canPlace(type==PlayerType.BLUE?this.battle.red_spawn_location:this.battle.blue_spawn_location,e.getBlock().getLocation())){
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("この地点では旗を置くことができません");
                }else{
                    if(type==PlayerType.BLUE){
                        RedBanner=e.getBlock().getLocation();
                        red_banner_timer=new BannerRunnable("アプサラス",0,battle.getConfig().getInt("Time"),ChatColor.DARK_RED,this.battle);
                        red=Bukkit.getScheduler().runTaskTimer(this.battle,red_banner_timer,0,60*60*20);
                    }else{
                        BlueBanner=e.getBlock().getLocation();
                        blue_banner_timer=new BannerRunnable("アルティオ",0,battle.getConfig().getInt("Time"),ChatColor.DARK_BLUE,this.battle);
                        blue=Bukkit.getScheduler().runTaskTimer(this.battle,blue_banner_timer,0,60*60*20);
                    }
                }
            }else{
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.DARK_RED+"あなたは旗を置くことができません");
            }
        }
    }
    @EventHandler
    public void breakEvent(BlockBreakEvent e){
        if(BlueBanner!=null) {
            if (BlueBanner.getBlock().getType() != Material.RED_BANNER) {
                Bukkit.getScheduler().cancelTask(blue.getTaskId());
            }
        }
        if(RedBanner!=null) {
            if (RedBanner.getBlock().getType() != Material.BLUE_BANNER) {
                Bukkit.getScheduler().cancelTask(red.getTaskId());
            }
        }
    }
    private boolean canPlace(Location spawnLocation,Location location){
        double x=Math.abs(location.getX()-spawnLocation.getX());
        double y=Math.abs(location.getX()-spawnLocation.getY());
        double z=Math.abs(location.getX()-spawnLocation.getZ());
        return x<=canXZr&&y<=canYr&&z<=canXZr&&cantXZr<x&&cantYr<y&&cantXZr<z;
    }
}
