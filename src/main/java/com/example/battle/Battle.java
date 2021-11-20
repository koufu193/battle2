package com.example.battle;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Battle extends JavaPlugin {
    util util=new util(this);
    final String KISHI_AKASHI_NAME="騎士の証";
    final String SAKIMORI_AKASHI_NAME="防人の証";
    Scoreboard scoreboard;
    @Override
    public void onEnable() {
        saveDefaultConfig();
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
    //20x±10x20より上
    //30×±15x30以下
    //に旗おける
    //騎士の証や防人の証＜＝dark red or dark blue
    //騎士:攻撃力増加レベル1
    //防人:耐性レベル1
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
    //捨てたら消える(どのチームでも)
}
