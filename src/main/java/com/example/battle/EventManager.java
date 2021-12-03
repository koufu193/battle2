package com.example.battle;

import net.minecraft.server.v1_16_R3.InventoryEnderChest;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Comparator;

public class EventManager implements Listener {
    Battle battle;
    public EventManager(Battle battle){
        this.battle=battle;
    }
    @EventHandler
    public void joinEvent(PlayerJoinEvent e){
        if(this.battle.info.getColorByPlayerName(e.getPlayer().getUniqueId())==null){
            this.battle.info.addPlayer(e.getPlayer());
        }
    }
    @EventHandler
    public void pickupInventoryEvent(EntityPickupItemEvent e){
        if(e.getItem()!=null) {
            if (e.getItem().getItemStack().hasItemMeta()) {
                if (e.getEntity() instanceof Player) {
                    if (!this.battle.info.addEffect((Player) e.getEntity(), e.getItem().getItemStack().getItemMeta().getDisplayName())) {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void dropInventoryEvent(PlayerDropItemEvent e){
        if(e.getItemDrop().getItemStack().hasItemMeta()){
            if(e.getItemDrop().getItemStack().getItemMeta().getDisplayName().matches("§."+this.battle.KISHI_AKASHI_NAME)){
                this.battle.kishi_sakimori_data.get(this.battle.KISHI_AKASHI_NAME).remove(e.getPlayer().getName());
                e.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }else if(e.getItemDrop().getItemStack().getItemMeta().getDisplayName().matches("§."+this.battle.SAKIMORI_AKASHI_NAME)){
                this.battle.kishi_sakimori_data.get(this.battle.SAKIMORI_AKASHI_NAME).remove(e.getPlayer().getName());
                e.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            }
        }
    }
    @EventHandler
    public void moveInventoryEvent(InventoryDragEvent e){

    }
    @EventHandler
    public void clickInventoryEvent(InventoryClickEvent e){
        if(e.getCurrentItem()!=null){
            if(e.getCurrentItem().hasItemMeta()&&this.battle.info.getColorByPlayerName(e.getWhoClicked().getUniqueId())!=null){
                PlayerType type=this.battle.info.getColorByPlayerName(e.getWhoClicked().getUniqueId());
                if(e.getCurrentItem().getItemMeta().getDisplayName().matches(type.isBoumei()?"§.":type.getChangeColor()+"[("+this.battle.KISHI_AKASHI_NAME+")("+this.battle.SAKIMORI_AKASHI_NAME+")]")){
                    e.setCancelled(true);
                }
            }
        }
    }
}
