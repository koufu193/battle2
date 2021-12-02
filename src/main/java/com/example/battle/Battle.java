package com.example.battle;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class Battle extends JavaPlugin {
    util util=new util(this);
    final String KISHI_AKASHI_NAME="騎士の証";
    final String SAKIMORI_AKASHI_NAME="防人の証";
    Scoreboard scoreboard;
    Map<String, Set<String>> kishi_sakimori_data=new HashMap<>();
    PlayerInfo info=new PlayerInfo(this);
    @Override
    public void onEnable() {
        saveDefaultConfig();

        kishi_sakimori_data.put(KISHI_AKASHI_NAME,new HashSet<>());
        kishi_sakimori_data.put(SAKIMORI_AKASHI_NAME,new HashSet<>());
        scoreboard= Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboard.registerNewTeam("red_team").setPrefix("DARK_RED");
        scoreboard.registerNewTeam("blue_team").setPrefix("DARK_BLUE");
        Bukkit.getPluginManager().registerEvents(new EventManager(this),this);
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
    //エフェクト:10秒ごとぐらいに証を持っている人に対応するエフェクトだす(12秒ごと)
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
}
