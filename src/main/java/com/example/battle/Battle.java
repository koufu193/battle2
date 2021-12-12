package com.example.battle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Battle extends JavaPlugin {
    util util=new util(this);
    final String KISHI_AKASHI_NAME="騎士の証";
    final String SAKIMORI_AKASHI_NAME="防人の証";
    Scoreboard scoreboard;
    Map<String, Set<UUID>> kishi_sakimori_data=new HashMap<>();
    PlayerInfo info=new PlayerInfo(this);
    Game game=new Game(this);
    AtomicBoolean canStart=new AtomicBoolean(true);
    AtomicBoolean isStart=new AtomicBoolean(false);
    EventManager manager=new EventManager(this);
    Location blue_spawn_location;
    Location red_spawn_location;
    Map<PlayerType,List<Location>> chestData=new HashMap<>();
    UUIDFile file=new UUIDFile(this);
    @Override
    public void onEnable() {
        saveDefaultConfig();
        if(!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
        List<Location> data=new ArrayList<>();
        for(String str:getConfig().getConfigurationSection("Locations.redChestLocations").getKeys(false)){
            data.add(this.util.getLocationByConfig(getConfig(),"Locations.redChestLocations",str));
        }
        chestData.put(PlayerType.RED,data);
        List<Location> data1=new ArrayList<>();
        for(String str:getConfig().getConfigurationSection("Locations.blueChestLocations").getKeys(false)){
            data1.add(this.util.getLocationByConfig(getConfig(),"Locations.blueChestLocations",str));
        }
        chestData.put(PlayerType.BLUE,data1);
        blue_spawn_location=util.getLocationByConfig(getConfig(),"Locations","blueLocation");
        red_spawn_location=util.getLocationByConfig(getConfig(),"Locations","redLocation");
        kishi_sakimori_data.put(KISHI_AKASHI_NAME,new HashSet<>());
        kishi_sakimori_data.put(SAKIMORI_AKASHI_NAME,new HashSet<>());
        scoreboard=Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboard.registerNewTeam("red_team").setPrefix(ChatColor.DARK_RED.toString());
        scoreboard.registerNewTeam("blue_team").setPrefix(ChatColor.DARK_BLUE.toString());
        scoreboard.registerNewTeam("boumei_team").setPrefix(ChatColor.WHITE.toString());
        if(file.isStarted()){
            file.setData();
            Bukkit.getPluginManager().registerEvents(this.manager,this);
            Bukkit.getScheduler().runTaskTimer(this,new Runnable(){
                PotionEffect kishi=new PotionEffect(PotionEffectType.INCREASE_DAMAGE,90*20,1);
                PotionEffect sakimori=new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,90*20,1);
                @Override
                public void run() {
                    for (String str : kishi_sakimori_data.keySet()) {
                        kishi_sakimori_data.get(str).forEach(b -> Bukkit.getPlayer(b).addPotionEffect(str.equals(KISHI_AKASHI_NAME) ? kishi : sakimori));
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },0,60*20);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("battle")){
            if (args.length==3) {
                if(args[0].equals("give")) {
                    if (sender instanceof BlockCommandSender) {
                        for (Entity entity:((BlockCommandSender) sender).getBlock().getLocation().getWorld().getNearbyEntities(((BlockCommandSender) sender).getBlock().getLocation(),2,2,2)){
                            if (!(entity instanceof Player)) {
                                continue;
                            }
                            if (args[1].equals("sakimori")) {
                                ItemStack item = new ItemStack(Material.PAPER);
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName(ChatColor.valueOf(args[2])+SAKIMORI_AKASHI_NAME);
                                item.setItemMeta(meta);
                                entity.getWorld().dropItem(entity.getLocation(), item);
                            } else if (args[1].equals("kishi")) {
                                ItemStack item = new ItemStack(Material.PAPER);
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName(ChatColor.valueOf(args[2])+KISHI_AKASHI_NAME);
                                item.setItemMeta(meta);
                                entity.getWorld().dropItem(entity.getLocation(), item);
                            } else if (args[1].equals("boumei")) {
                                ItemStack item = new ItemStack(Material.PAPER);
                                ItemMeta meta = item.getItemMeta();
                                if (ChatColor.valueOf(args[2])==ChatColor.DARK_RED) {
                                    meta.setDisplayName(ChatColor.DARK_RED+"アルティオへの亡命書");
                                } else if (ChatColor.valueOf(args[2])==ChatColor.DARK_BLUE) {
                                    meta.setDisplayName(ChatColor.DARK_BLUE+"アプサラスへの亡命書");
                                }
                                item.setItemMeta(meta);
                                entity.getWorld().dropItem(entity.getLocation(), item);
                            }
                        }

                    }
                    return false;
                }
            }
            if(args.length==1){
                if(args[0].equals("start")) {
                    if (isStart.get()) {
                        sender.sendMessage("すでに試合は始まっています");
                    }else if(!canStart.get()){
                        sender.sendMessage("ワールド生成用のクールダウン中です");
                    }else{
                        Bukkit.broadcastMessage(ChatColor.GREEN+"試合開始");
                        this.game.startGame();
                    }
                }else if(args[0].equals("reload")) {
                    sender.sendMessage("リロード開始");
                    onDisable();
                    onEnable();
                    sender.sendMessage("リロード終了");
                    return true;
                }else if(args[0].equals("tp")) {
                    if (sender instanceof Player) {
                        if (Bukkit.getWorld(getConfig().getString("newWorldName")) != null) {
                            Location location = ((Player) sender).getLocation().clone();
                            location.setWorld(Bukkit.getWorld(getConfig().getString("newWorldName")));
                            ((Player) sender).teleport(location);
                            return true;
                        } else {
                            sender.sendMessage("ワールドが見つかりませんでした");
                        }
                    } else {
                        sender.sendMessage("プレーヤーしか実行できません");
                    }
                }else if(args[0].equals("stop")) {
                    if (isStart.get()) {
                        this.game.finishGame();
                        Bukkit.getScheduler().runTaskLater(this, bukkitTask -> this.canStart.set(true), this.getConfig().getInt("waitTime") * 20);
                        Bukkit.broadcastMessage(ChatColor.RED + "試合が強制終了されました");
                    } else {
                        sender.sendMessage("試合が始まっていません");
                    }
                }else{
                    sender.sendMessage("コマンドの構文が違います(battle <start/reload/tp/stop>)");
                }
            }else{
                sender.sendMessage("コマンドの構文が違います(battle <start/reload/tp/stop>)");
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        saveConfig();
        reloadConfig();
        if(isStart.get()){
            file.saveData();
        }
    }
    //-4000 65 0 <-blue spawn
    //4000 65 0 <-red spawn
    //20x±10x20以下は旗置けない
    //30×±15x30以下で旗置ける
    //騎士の証や防人の証＜＝dark red or dark blue
    //騎士:攻撃力増加レベル1 <= done
    //防人:耐性レベル1 <= done
    //エフェクト:10秒ごとぐらいに証を持っている人に対応するエフェクトだす(12秒ごと)<=done
    //↑初めはイベントで証をゲットしたときにエフェクト出す(12秒)<-done
    //アプサラスへの亡命書(青)<-dark blue<-done
    //アルティオへの亡命書(赤)<-dark red<-done
    //アプサラス制圧旗(青色の旗)<-dark blue
    //アルティオ制圧旗(赤色の旗),-dark red
    //おかれたら旗は1時間ごとにアナウンス、24時間経過で終了
    //()は除く
    //制圧を示す制圧旗が立てられ○○時間が経過しました
    //無所属の場合：その人が持っている証は消す<-done
    //捨てたら同じチームの人は拾えるがそれ以外の人は拾えない<-done
    //チェストなどに証をためて違う色になった後にチェストから違う色の証を取ろうとしたら消える<-done
    //防人の証と騎士の証両方持っていたら両方削除<-done
    //ワールド名はconfigから<-done
    //旗は一本しかさせない
    //無所属で死んだり亡命捨てたらインベントリクリア<-done
    //所属の場合死んだらそのままロスト<-done
}
