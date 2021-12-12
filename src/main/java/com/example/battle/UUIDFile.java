package com.example.battle;

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
                    this.battle.info.playerColor.put(uuid,PlayerType.valueOf(data[1]));
                    if(!data[2].equals("none")){
                        this.battle.kishi_sakimori_data.get(data[2]).add(uuid);
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(this.battle.getDataFolder(),"save.txt")))) {
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
    }
    public boolean isStarted(){
        return new File(this.battle.getDataFolder(),"save.txt").exists();
    }
}
