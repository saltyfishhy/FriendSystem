package me.saltyfishhy.friendSystem.Commands;

import me.saltyfishhy.friendSystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class R implements CommandExecutor {

    public static HashMap<Player, Player> respond = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("msg")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                String uuid = p.getUniqueId().toString();
                if (args.length == 0) {
                    p.sendMessage(ChatColor.RED + "Message > " + ChatColor.GRAY + "Proper usage: /msg (user) (message)");
                    return true;
                }
                else if (args.length == 1) {
                    p.sendMessage(ChatColor.RED + "Message > " + ChatColor.GRAY + "Proper usage: /msg (user) (message)");
                    return true;
                }
                else {
                    for (Player p2 : Bukkit.getOnlinePlayers()) {
                        Player p3;
                        try {
                            p3 = Bukkit.getPlayer(args[0]);
                        } catch (Exception e) {
                            p.sendMessage(ChatColor.RED + "Message > " + ChatColor.GRAY + "The specified player is not online.");
                            return true;
                        }
                        if (p3.getName().equals(p2.getName())) {
                            List<String> friends = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".friends"));
                            if (friends.contains(p3.getUniqueId().toString())) {
                                String message = "";
                                for (int i = 1; i < args.length; i++) {
                                    message = message + args[i] + " ";
                                }
                                p3.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_GRAY + p.getName() + ChatColor.GOLD + " -> " + ChatColor.DARK_GRAY + "You" + ChatColor.GOLD + "]" + ChatColor.LIGHT_PURPLE + " " + ChatColor.LIGHT_PURPLE + message);
                                p.sendMessage(ChatColor.GOLD + "[" + ChatColor.DARK_GRAY + "You" + ChatColor.GOLD + " -> " + ChatColor.DARK_GRAY + p3.getName() + ChatColor.GOLD + "]" + ChatColor.LIGHT_PURPLE + " " + ChatColor.LIGHT_PURPLE + message);
                                respond.put(p, p3);
                                respond.put(p3, p);
                                return true;
                            }
                        }
                    }
                    p.sendMessage(ChatColor.RED + "Message > " + ChatColor.GRAY + "You are not friends with the specified player.");
                    return true;
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("reply")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (respond.containsKey(p)) {
                    if (args.length < 1) {
                        p.sendMessage(ChatColor.RED + "Message > " + ChatColor.GRAY + "Proper usage: /r (message)");
                        return true;
                    } else {
                        String message = "";
                        for (int i = 0; i < args.length; i++) {
                            message = message + args[i] + " ";
                        }
                        p.chat("/msg " + respond.get(p).getName() + " " + message);
                        return true;
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "Message > " + ChatColor.GRAY + "No one has messaged you recently!");
                    return true;
                }
            }
        }
        return false;
    }

}
