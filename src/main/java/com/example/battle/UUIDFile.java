package com.example.battle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.UUID;

public class UUIDFile {
    Battle battle;
    //UUID(String) PlayerType Type(SAKIMORI,KISHI,NONE)
    public UUIDFile(Battle battle){
        this.battle=battle;
    }
    public void setData(){
        if(isStarted()){
            this.battle.isStart.set(true);
            try(BufferedReader reader=new BufferedReader(new FileReader(new File(this.battle.getDataFolder(),"save.txt")))){
                String line;
                while((line=reader.readLine())!=null){
                    String[] data=line.split(" ",3);
                    UUID uuid=UUID.fromString(data[0]);
                    OfflinePlayer p=Bukkit.getOfflinePlayer(uuid);
                    if(p!=null) {
                        PlayerType type = PlayerType.valueOf(data[1]);
                        this.battle.info.playerColor.put(uuid, type);
                        this.battle.scoreboard.getTeam((type == PlayerType.BLUE ? "blue" : type == PlayerType.RED ? "red" : "boumei") + "_team").addEntry(p.getName());
                        if (!data[2].equals("none")) {
                            this.battle.kishi_sakimori_data.get(data[2]).add(uuid);
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            try(BufferedReader reader=new BufferedReader(new FileReader(new File(this.battle.getDataFolder(),"banner.txt")))){
                String line;
                for(int i=0;i<2;i++){
                    line=reader.readLine();
                    if(line!=null&&!line.equals("null")) {
                        String[] data = line.split(" ", 6);
                        if(i==0) {
                            this.battle.event.RedBanner = new Location(Bukkit.getWorld(this.battle.util.getWorldName()), Double.parseDouble(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]));
                            this.battle.event.red_banner_timer = new BannerRunnable(data[4], Integer.parseInt(data[3]), this.battle.getConfig().getInt("Time"), ChatColor.valueOf(data[5]), this.battle);
                            this.battle.event.red=Bukkit.getScheduler().runTaskTimer(this.battle,this.battle.event.red_banner_timer,0,60*60*20);
                        }else{
                            this.battle.event.BlueBanner = new Location(Bukkit.getWorld(this.battle.util.getWorldName()), Double.parseDouble(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]));
                            this.battle.event.blue_banner_timer = new BannerRunnable(data[4], Integer.parseInt(data[3]), this.battle.getConfig().getInt("Time"), ChatColor.valueOf(data[5]), this.battle);
                            this.battle.event.blue=Bukkit.getScheduler().runTaskTimer(this.battle,this.battle.event.blue_banner_timer,0,60*60*20);
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(this.battle.getDataFolder(),"save.txt"),false))) {
            for (UUID uuid : this.battle.info.playerColor.keySet()) {
                String type = null;
                for (String key : this.battle.kishi_sakimori_data.keySet()) {
                    if (this.battle.kishi_sakimori_data.get(key).contains(uuid)) {
                        type = key;
                    }
                }
                writer.write(uuid.toString()+" "+this.battle.info.getColorByPlayerName(uuid).name()+" "+(type!=null?type:"none")+"\n");
            }
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(this.battle.getDataFolder(),"banner.txt"),false))) {
            if(this.battle.event.red!=null){
                System.out.println("カキコ");
                writer.write(this.battle.event.RedBanner.getX()+" "+this.battle.event.RedBanner.getY()+" "+this.battle.event.RedBanner.getZ()+" "+this.battle.event.red_banner_timer.now+" "+this.battle.event.red_banner_timer.title+" RED"+System.lineSeparator());
            }else{
                writer.write("null"+System.lineSeparator());
            }
            if(this.battle.event.blue!=null){
                System.out.println("カキコ");
                writer.write(this.battle.event.BlueBanner.getX()+" "+this.battle.event.BlueBanner.getY()+" "+this.battle.event.BlueBanner.getZ()+" "+this.battle.event.blue_banner_timer.now+" "+this.battle.event.blue_banner_timer.title+" RED"+System.lineSeparator());
            }else{
                writer.write("null"+System.lineSeparator());
            }
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public boolean isStarted(){
        return new File(this.battle.getDataFolder(),"save.txt").exists();
    }
}
