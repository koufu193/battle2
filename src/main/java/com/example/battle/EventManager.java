package com.example.battle;

import net.minecraft.server.v1_16_R3.InventoryEnderChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class EventManager implements Listener {
    Battle battle;
    public EventManager(Battle battle){
        this.battle=battle;
    }
    @EventHandler
    public void pickupInventoyEvent(EntityPickupItemEvent e){

    }
    @EventHandler
    public void dropInventoyEvent(PlayerDropItemEvent e){
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
    public void moveInventoyEvent(InventoryMoveItemEvent e){

    }
}
