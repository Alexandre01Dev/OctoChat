package be.alexandre01.octosia.octochat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SocialSpy implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
                if(cmd.getName().equalsIgnoreCase("octochatspy")){
                if(sender.hasPermission("octochat.see")){
                    if(Plugin.getPlugin().spyMsg.contains(player)){
                        sender.sendMessage("OctoSpy >> Désactivé");
                        Plugin.getPlugin().spyMsg.remove(player);
                    }else {
                        sender.sendMessage("OctoSpy >> Activé");
                        Plugin.getPlugin().spyMsg.add(player);
                    }

                }
            }
        }

        return false;
    }
}
