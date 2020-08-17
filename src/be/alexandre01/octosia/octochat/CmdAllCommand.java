package be.alexandre01.octosia.octochat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CmdAllCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if(cmd.getName().equalsIgnoreCase("allCmd")){
            if(args.length > 0){
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < args.length; i++) {
                    sb.append(args[i]+" ");
                }
                ConsoleCommandSender cSend = Bukkit.getConsoleSender();
                for (Player player : Bukkit.getOnlinePlayers()){
                    Bukkit.dispatchCommand(cSend,sb.toString().replaceAll("%player%",player.getName()));
                }
                sender.sendMessage("Commandes envoyé aux "+Bukkit.getOnlinePlayers().size()+" joueurs");
            }else {
                sender.sendMessage("§cVeuillez mettre un argument");
            }
        }
        if(cmd.getName().equalsIgnoreCase("randomCMD")){
            if(args.length > 0){
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < args.length-1; i++) {
                    sb.append(args[i]+" ");
                }
                System.out.println(sb.toString());
                ConsoleCommandSender cSend = Bukkit.getConsoleSender();
                ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                Collections.shuffle(players);
                int size;
                try {
                    size = Integer.parseInt(args[args.length-1]);
                }catch (Exception e){
                    sender.sendMessage("Veuillez marquer le nombre de joueur");
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    Bukkit.dispatchCommand(cSend,sb.toString().replaceAll("%player%",players.get(i).getName()));
                }
                sender.sendMessage("Commandes envoyé aux "+size+" joueurs");
            }else {
                sender.sendMessage("§cVeuillez mettre un argument");
            }
        }
        return false;
    }
}
