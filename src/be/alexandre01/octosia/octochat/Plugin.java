package be.alexandre01.octosia.octochat;

import be.alexandre01.octosia.octochat.configs.YamlUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class Plugin extends JavaPlugin implements Listener {
    public static Plugin plugin;
    YamlUtils msg;
    ArrayList<String> autoBroadcastMSG = new ArrayList<>();
    int delay;
    HashMap<Player,Long> lastChat;

    HashMap<Player,Player> lastMsg;
    ArrayList<Player> spyMsg;
    ArrayList<Player> ytPub;
    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable(){
        plugin = this;

        msg = new YamlUtils(this,"messages.yml");
        lastMsg = new HashMap<>();
        lastChat = new HashMap<>();
        spyMsg = new ArrayList<>();
        getCommand("msg").setExecutor(new MessageCommand());
        getCommand("r").setExecutor(new MessageCommand());
        getServer().getPluginManager().registerEvents(this,this);
        getCommand("octochatspy").setExecutor(new SocialSpy());
        getCommand("allcmd").setExecutor(new CmdAllCommand());
        getCommand("randomcmd").setExecutor(new CmdAllCommand());
        for (String s : msg.getConfig().getStringList("Messages")){
            autoBroadcastMSG.add(s.replaceAll("&","§"));
        }

        delay = msg.getConfig().getInt("Delay");
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new BukkitRunnable() {
            @Override
            public void run() {
                String broadcast = autoBroadcastMSG.get(0);
                BaseComponent baseComponent = new TextComponent(TextComponent.fromLegacyText(""));
                baseComponent.addExtra("*§7-----------------------------------------------§f*\n");
                baseComponent.addExtra(new TextComponent(TextComponent.fromLegacyText(broadcast)));
                baseComponent.addExtra("*§7-----------------------------------------------§f*");
               baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,TextComponent.fromLegacyText("§6Message Automatique")));
               if(broadcast.toLowerCase().contains("http")){
                  baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,"http"+broadcast.split("http")[1]));
               }
                Bukkit.spigot().broadcast(baseComponent);
                autoBroadcastMSG.remove(0);
                autoBroadcastMSG.add(broadcast);
            }
        },delay,delay);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!player.hasPermission("octochat.bypass")){
            lastChat.put(player,new Date().getTime());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        lastMsg.remove(player);
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        long ms = new Date().getTime();
        if(lastChat.containsKey(player)){
            if( TimeUnit.MILLISECONDS.toSeconds(ms-lastChat.get(player)) <= 3){
                player.sendMessage("§cVeuillez attendre 3 secondes avant d'envoyer un nouveau message");
                event.setCancelled(true);
            }else {
                lastChat.put(player,ms);
            }
        }
    }

}
