package com.example.battle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
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
    boolean isStart=false;
    EventManager manager=new EventManager(this);
    Location blue_spawn_location;
    Location red_spawn_location;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        blue_spawn_location=util.getLocationByConfig(getConfig(),"Locations","blueLocation");
        red_spawn_location=util.getLocationByConfig(getConfig(),"Locations","redLocation");
        kishi_sakimori_data.put(KISHI_AKASHI_NAME,new HashSet<>());
        kishi_sakimori_data.put(SAKIMORI_AKASHI_NAME,new HashSet<>());
        scoreboard=Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboard.registerNewTeam("red_team").setPrefix(ChatColor.DARK_RED.toString());
        scoreboard.registerNewTeam("blue_team").setPrefix(ChatColor.DARK_BLUE.toString());
        scoreboard.registerNewTeam("boumei_team").setPrefix(ChatColor.WHITE.toString());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("battle")){
            if(args.length==1){
                if(args[0].equals("start")) {
                    if (isStart) {
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
                }else if(args[0].equals("stop")){
                    if(isStart){
                        this.game.finishGame();
                        Bukkit.broadcastMessage(ChatColor.RED+"試合が強制終了されました");
                    }else{
                        sender.sendMessage("試合が始まっていません");
                    }
                }else{
                    sender.sendMessage("コマンドの構文が違います(battle <start/reload/tp/stop>");
                }
            }else{
                sender.sendMessage("コマンドの構文が違います(battle <start/reload/tp/stop>");
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
    //-4000 65 0 <-blue spawn
    //4000 65 0 <-red spawn
    //20x±10x20以下は旗置けない
    //30×±15x30以下で旗置ける
    //騎士の証や防人の証＜＝dark red or dark blue
    //騎士:攻撃力増加レベル1 <= done
    //防人:耐性レベル1 <= done
    //エフェクト:10秒ごとぐらいに証を持っている人に対応するエフェクトだす(12秒ごと)<=done
    //↑初めはイベントで証をゲットしたときにエフェクト出す(12秒)
    //アプサラスへの亡命書(青)<-dark blue
    //アルティオへの亡命書(赤)<-dark red
    //アプサラス制圧旗(青色の旗)<-dark blue
    //アルティオ制圧旗(赤色の旗),-dark red
    //おかれたら旗は1時間ごとにアナウンス、24時間経過で終了
    //()は除く
    //制圧を示す制圧旗が立てられ○○時間が経過しました
    //無所属の場合：その人が持っている証は消す
    //捨てたら同じチームの人は拾えるがそれ以外の人は拾えない
    //チェストなどに証をためて違う色になった後にチェストから違う色の証を取ろうとしたら消える
    //防人の証と騎士の証両方持っていたら両方削除
    //ワールド名はconfigから
    //旗は一本しかさせない
    //無所属で死んだり亡命捨てたらインベントリクリア
    //所属の場合死んだらそのままロスト
}
