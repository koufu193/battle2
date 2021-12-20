package com.example.battle;


import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageSentEvent;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.*;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.List;
import java.util.UUID;

public class DiscordSRV {
    Battle battle;
    TextChannel channel;
    public DiscordSRV(Battle battle){
        this.battle=battle;
        channel = github.scarsz.discordsrv.DiscordSRV.getPlugin().getMainTextChannel();
    }
    public void showText(String text, Color color){
        text=text.replaceAll("§.","");
        if(channel==null){
            channel = github.scarsz.discordsrv.DiscordSRV.getPlugin().getMainTextChannel();
        }
        if(channel!=null){
            EmbedBuilder builder=new EmbedBuilder();
            MessageEmbed embed=builder.setColor(color).setDescription(text).build();
            channel.sendMessageEmbeds(embed).queue();
        }
    }
    @Subscribe
    public void DiscordMessageEvent(DiscordGuildMessageReceivedEvent e){
        if(e.getMessage().getContentDisplay().equals("k/info")){
            if(this.battle.isStart.get()) {
                MessageEmbed embed = new EmbedBuilder().setColor(Color.WHITE).setDescription("アルティオチーム:" + (this.battle.event.red == null ? "旗はまだ刺されていません" : "残り時間" + (this.battle.event.red_banner_timer.max.get() - this.battle.event.red_banner_timer.now.get()) + "時間") + "\n" + "アプサラスチーム:" + (this.battle.event.blue == null ? "旗はまだ刺されていません" : "残り時間" + (this.battle.event.blue_banner_timer.max.get() - this.battle.event.blue_banner_timer.now.get()) + "時間")).build();
                e.getChannel().sendMessageEmbeds(embed).queue();
            }else{
                MessageEmbed embed=new EmbedBuilder().setColor(Color.WHITE).setDescription("試合はまだ始まっていません").build();
                e.getChannel().sendMessageEmbeds(embed).queue();
            }
        }
    }
    public void changeRoll(UUID uuid){
        PlayerType type=this.battle.info.getColorByPlayerName(uuid);
        String id=github.scarsz.discordsrv.DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(uuid);
        if(id!=null) {
            User user = github.scarsz.discordsrv.DiscordSRV.getPlugin().getJda().getUserById(id);
            if (user != null) {
                Guild guild = github.scarsz.discordsrv.DiscordSRV.getPlugin().getMainGuild();
                Member member = guild.getMember(user);
                if (type.getColor() == ChatColor.DARK_RED) {
                    guild.addRoleToMember(member, guild.getRoleById("906676229892108299")).queue();
                    guild.removeRoleFromMember(member, guild.getRoleById("906676570234716260")).queue();
                } else if (type.getColor() == ChatColor.DARK_BLUE) {
                    guild.addRoleToMember(member, guild.getRoleById("906676570234716260")).queue();
                    guild.removeRoleFromMember(member, guild.getRoleById("906676229892108299")).queue();
                }
                this.battle.getLogger().info("user:"+user.getName()+" id:"+id+" role:"+guild.getRoleById("906676229892108299").getName()+" "+guild.getRoleById("906676570234716260").getName());
            }else{
                this.battle.getLogger().info("user is null");
            }
        }else{
            this.battle.getLogger().info("id is null");
        }
    }
    public void finishGame(){
        Guild guild=github.scarsz.discordsrv.DiscordSRV.getPlugin().getMainGuild();
        for(UUID uuid:this.battle.info.playerColor.keySet()){
            Member member=guild.getMember(guild.getJDA().getUserById(github.scarsz.discordsrv.DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(uuid)));
            PlayerType type=this.battle.info.playerColor.get(uuid).getBeenColor();
            guild.removeRoleFromMember(member,guild.getRoleById(type==PlayerType.BLUE?"906676570234716260":"906676229892108299")).queue();
        }
    }
}
