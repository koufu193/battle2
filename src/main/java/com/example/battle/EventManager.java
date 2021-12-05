package com.example.battle;

import net.minecraft.server.v1_16_R3.InventoryEnderChest;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
            }else if(e.getItemDrop().getItemStack().getItemMeta().getDisplayName().matches("[("+ChatColor.DARK_RED+"アルティオ)("+ChatColor.DARK_BLUE+"アプサラス)]への亡命書")){
                e.getItemDrop().remove();
                PlayerType type=this.battle.info.getColorByPlayerName(e.getPlayer().getUniqueId());
                if(type==null){
                    e.getPlayer().sendMessage("亡命していないのに亡命所を使わないでください");
                }else if(type.isBoumei()) {
                    this.battle.info.backColor(e.getPlayer(), false);
                }
            }
        }
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
    @EventHandler
    public void closeInventoryEvent(InventoryCloseEvent e){
        PlayerType type=this.battle.info.getColorByPlayerName(e.getPlayer().getUniqueId());
        boolean hasBoumei=false;
        for(ItemStack item:e.getPlayer().getInventory().getContents()){
            if(item!=null){
                if(item.hasItemMeta()){
                    if(item.getItemMeta().getDisplayName().matches("§.[("+this.battle.SAKIMORI_AKASHI_NAME+")("+this.battle.KISHI_AKASHI_NAME+")]")) {
                        if (!this.battle.info.addEffect((Player) e.getPlayer(), item.getItemMeta().getDisplayName())) {
                            e.getPlayer().getInventory().remove(item);
                        }
                    }
                    if(type!=null&&!hasBoumei){
                        if(type.isBoumei()){
                            if(item.getItemMeta().getDisplayName().matches("[("+ChatColor.DARK_RED+"アルティオ)("+ChatColor.DARK_BLUE+"アプサラス)]への亡命書")){
                                hasBoumei=true;
                            }
                        }else{
                            hasBoumei=true;
                        }
                    }
                }
            }
        }
        if(!hasBoumei&&type.isBoumei()){
            this.battle.info.backColor((Player) e.getPlayer(),false);
            for(ItemStack item:e.getView().getTopInventory().getContents()){
                if(item!=null) {
                    if(item.hasItemMeta()) {
                        if (item.getItemMeta().getDisplayName().matches("[(" + ChatColor.DARK_RED + "アルティオ)(" + ChatColor.DARK_BLUE + "アプサラス)]への亡命書")) {
                            e.getView().getBottomInventory().remove(item);
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void boumeievent(PlayerPortalEvent e){
        e.setCancelled(e.getCause()!=PlayerTeleportEvent.TeleportCause.PLUGIN);
        PlayerType type=this.battle.info.getColorByPlayerName(e.getPlayer().getUniqueId());
        if(type!=null&&e.getCause()== PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            if (this.battle.info.getColorByPlayerName(e.getPlayer().getUniqueId()).isBoumei()&&(type.getBeenColor()==PlayerType.BLUE?this.battle.red_spawn_location:this.battle.blue_spawn_location).distance(e.getPlayer().getLocation())<=2){
                this.battle.info.changePlayer(e.getPlayer());
            }
        }
    }
    @EventHandler
    public void hurtPlayerEvent(EntityDamageByEntityEvent e){
        PlayerType type=this.battle.info.getColorByPlayerName(e.getEntity().getUniqueId());
        PlayerType typeDamage=this.battle.info.getColorByPlayerName(e.getDamager().getUniqueId());
        if(type!=null&&typeDamage!=null){
            if(!type.isBoumei()&&!typeDamage.isBoumei()&&type==typeDamage){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void ClickEvent(PlayerInteractEvent e){
        if(e.getAction()==Action.RIGHT_CLICK_BLOCK&&!(e.getPlayer().isSneaking()&&e.hasItem())){
            if(e.getClickedBlock().getType()== Material.CHEST){
                PlayerType type=this.battle.info.getColorByPlayerName(e.getPlayer().getUniqueId());
                Location location=e.getClickedBlock().getLocation();
                e.setCancelled(this.battle.chestData.entrySet().stream().filter(a->a.getKey()!=type).anyMatch(a->a.getValue().stream().anyMatch(b->b.getX()==location.getX()&&b.getY()==location.getY()&&b.getZ()==location.getZ())));
            }
        }
    }
    @EventHandler
    public void RespawnEvent(PlayerRespawnEvent e){
        PlayerType type=this.battle.info.getColorByPlayerName(e.getPlayer().getUniqueId());
        if(type!=null){
            e.getPlayer().teleport(type==PlayerType.BLUE?this.battle.blue_spawn_location:this.battle.red_spawn_location);
        }
    }
    @EventHandler
    public void explodeEvent(EntityExplodeEvent e){
        e.blockList().removeIf(b->b.hasMetadata("MAMORI"));
    }
    @EventHandler
    public void deathEvent(PlayerDeathEvent e){
        if(this.battle.info.getColorByPlayerName(e.getEntity().getUniqueId()).isBoumei()){
            this.battle.info.backColor(e.getEntity(),true);
        }
    }
}
