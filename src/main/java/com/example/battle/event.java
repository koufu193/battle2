package com.example.battle;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class event implements Listener {
    Battle battle;
    public event(Battle battle){
        this.battle=battle;
    }
    @EventHandler
    public void JoinEvent(PlayerJoinEvent e){
        if(!this.battle.playerData.containsKey(e.getPlayer().getUniqueId().toString())){
            ChatColor color=getColor();
            this.battle.playerData.put(e.getPlayer().getUniqueId().toString(),color);
            e.getPlayer().setPlayerListName(color+e.getPlayer().getPlayerListName()+ChatColor.RESET);
            e.getPlayer().setDisplayName(color+e.getPlayer().getDisplayName()+ChatColor.RESET);
            this.battle.scoreboard.getTeam(color==ChatColor.RED?"red":"blue").addPlayer(e.getPlayer());
            if (color==ChatColor.RED) {
                battle.redteam.add(e.getPlayer());
            } else if (color==ChatColor.BLUE){
                battle.blueteam.add(e.getPlayer());
            }
        }
    }
    @EventHandler
    public void placeevent(BlockPlaceEvent e){
        if(110<e.getBlockPlaced().getLocation().getY()){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void PistonEvent(BlockPistonExtendEvent e){
        if(e.getDirection()== BlockFace.UP){
            for(Block b:e.getBlocks()){
                if(110<b.getLocation().getY()+1){
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
    public ChatColor getColor(){
        if(this.battle.number[0]<this.battle.number[1]){
            this.battle.number[0]++;
            return ChatColor.RED;
        }
        this.battle.number[1]++;
        return ChatColor.BLUE;
    }
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {

    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        if (e.getBlock().getLocation()==battle.diamond||e.getBlock().getLocation()==battle.iron||e.getBlock().getLocation()==battle.gold) {
            e.getBlock().setType(e.getBlock().getType());
        }
    }
}
