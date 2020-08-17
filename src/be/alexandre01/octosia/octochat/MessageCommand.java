package be.alexandre01.octosia.octochat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(cmd.getName().equalsIgnoreCase("msg")){
                if(args.length < 2){
                    sender.sendMessage("§cVeuillez préciser un joueur et votre message");
                    return true;
                }
                    Player target;
                    try {
                        target = Bukkit.getPlayer(args[0]);
                    }catch (Exception e){
                        player.sendMessage("§cVeuillez mettre un joueur valide");
                        return true;
                    }
                    StringBuffer sb = new StringBuffer();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]+" ");
                    }
                    sendMessage(player,target,sb.toString());
                    Plugin.getPlugin().lastMsg.put(player, target);
                    Plugin.getPlugin().lastMsg.put(target, player);

            }
            if(cmd.getName().equalsIgnoreCase("r")){
                if(args.length == 0){
                    sender.sendMessage("§cVeuillez préciser votre message");
                    return true;
                }
                    if(!Plugin.getPlugin().lastMsg.containsKey(player)){
                        player.sendMessage("§cVous n'avez aucune personne à qui répondre.");
                        return true;
                    }
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < args.length; i++) {
                        sb.append(args[i]+" ");
                    }
                    Player target =Plugin.getPlugin().lastMsg.get(player);
                    if(!target.isOnline()){
                        player.sendMessage("§cLe joueur séléctionné est hors ligne");
                        return true;
                    }
                   sendMessage(player,target,sb.toString());
                    Plugin.getPlugin().lastMsg.put(target, player);
                }
        }

        return false;
    }

    private void sendMessage(Player sender, Player receiver,String message){
        sender.sendMessage("§8[§6Moi §8-> §e"+receiver.getName()+"§8] §7"+message);
        receiver.sendMessage("§8[§e"+sender.getName()+" §8-> §6Moi §8] §7"+message);
        for(Player player : Plugin.getPlugin().spyMsg){
            player.sendMessage("§7[SPY] §8[§6"+sender.getName()+" §8-> §e"+receiver.getName()+"§8] §7"+message);
        }
    }
}
