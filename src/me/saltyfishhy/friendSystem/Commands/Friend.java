package me.saltyfishhy.friendSystem.Commands;

import me.saltyfishhy.friendSystem.FileManager.FileManager;
import me.saltyfishhy.friendSystem.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class Friend implements CommandExecutor {


    public static HashMap<Integer, List<String>> friend1 = new HashMap<>();
    public static HashMap<Player, Integer> currPage = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("friend")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!(p.hasPermission(Main.getInstance().getConfig().getString("friendPermission")))) {
                    p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You do not have permission to execute this command.");
                    return true;
                }
                if (args.length > 0) {
                    if (!args[0].equalsIgnoreCase("accept")) {
                        if (!args[0].equalsIgnoreCase("deny")) {
                            String uuid = p.getUniqueId().toString();
                            List<String> friends = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".friends"));
                            if (!(friends.size() == Main.getInstance().getConfig().getInt("maxFriends"))) {
                                String uuid2;
                                Player targ;
                                try {
                                    targ = Bukkit.getPlayer(args[0]);
                                    uuid2 = targ.getUniqueId().toString();
                                } catch (Exception e) {
                                    p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "Unknown player. Make sure that you typed their name correctly or that they are online.");
                                    return true;
                                }
                                List<String> requests = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".requests"));
                                if (requests.contains(uuid2)) {
                                    p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You have already sent a request to this player!");
                                    return true;
                                } else {
                                    if (friends.contains(uuid2)) {
                                        p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You are already friends with this player!");
                                        return true;
                                    }
                                    targ.sendMessage(ChatColor.RED + "Friends > " + ChatColor.RED + p.getName() + ChatColor.GRAY + " has sent you a friend request! Type /friend accept " + p.getName() + " to accept it!");
                                    p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You have sent a friend request to " + ChatColor.RED + targ.getName());
                                    requests.add(uuid2);
                                    Main.data.getConfig().set("players." + uuid + ".requests", requests);
                                    List<String> invites = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid2 + ".invites"));
                                    invites.add(uuid);
                                    Main.data.getConfig().set("players." + uuid2 + ".invites", invites);
                                    Main.data.saveConfig();
                                    int timeout = Main.getInstance().getConfig().getInt("requestTimeout");
                                    timeout = timeout * 20;
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                                        public void run() {
                                            if (invites.contains(uuid)) {
                                                targ.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "Your friend request from " + ChatColor.RED + p.getName() + ChatColor.GRAY + " has expired.");
                                                p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "Your friend request to " + ChatColor.RED + targ.getName() + ChatColor.GRAY + " has expired.");
                                                Main.data.getConfig().set("players." + uuid + ".requests." + uuid2, null);
                                                Main.data.getConfig().set("players." + uuid2 + ".invites." + uuid, null);
                                                Main.data.saveConfig();
                                            }
                                        }
                                    }, timeout);
                                    return true;
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You already have the max amount of friends!");
                                return true;
                            }
                        } else {
                            String uuid = p.getUniqueId().toString();
                            List<String> invites = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".invites"));
                            if (invites.size() == 0) {
                                p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You do not have any current friend requests.");
                                return true;
                            } else {
                                if (args.length != 2) {
                                    p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "Please include the name of the player who sent the request.");
                                    return true;
                                }
                                String uuid3 = Bukkit.getOfflinePlayer(args[1]).getUniqueId().toString();
                                for (String name : Main.data.getConfig().getStringList("players." + uuid + ".invites")) {
                                    if (name.equals(uuid3)) {
                                        try {
                                            String uuid2 = name;
                                            UUID uuid4 = UUID.fromString(uuid2);
                                            Player targ = Bukkit.getPlayer(uuid4);
                                            List<String> friends = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".invites"));
                                            List<String> friends1 = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid2 + ".requests"));
                                            friends.remove(uuid2);
                                            friends1.remove(uuid2);
                                            Main.data.getConfig().set("players." + uuid + ".requests", friends);
                                            Main.data.getConfig().set("players." + uuid2 + ".invites", friends1);
                                            Main.data.saveConfig();
                                            p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You denied the friend request from " + ChatColor.RED + p.getName());
                                            targ.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "Your friend request to " + ChatColor.RED + targ.getName() + ChatColor.GRAY + " was denied");
                                            return true;
                                        } catch (Exception e) {
                                            String uuid2 = name;
                                            List<String> friends = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".invites"));
                                            List<String> friends1 = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid2 + ".requests"));
                                            List<String> denied = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid2 + ".denied"));
                                            friends.remove(uuid2);
                                            friends1.remove(uuid2);
                                            Main.data.getConfig().set("players." + uuid + ".requests", friends);
                                            Main.data.getConfig().set("players." + uuid2 + ".invites", friends1);
                                            Main.data.getConfig().set("players." + uuid2 + ".denied", denied);
                                            Main.data.saveConfig();
                                            p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You denied the friend request from " + ChatColor.RED + args[1]);
                                            return true;
                                        }
                                    }
                                }
                                p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You don't have a friend request from " + ChatColor.RED + args[1]);
                                return true;
                            }
                        }
                    } else {
                        String uuid = p.getUniqueId().toString();
                        List<String> invites = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".invites"));
                        if (invites.size() == 0) {
                            p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You do not have any current friend requests.");
                            return true;
                        } else {
                            if (args.length != 2) {
                                p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "Please include the name of the player who sent the request.");
                                return true;
                            }
                            String uuid3 = Bukkit.getOfflinePlayer(args[1]).getUniqueId().toString();
                            for (String name : Main.data.getConfig().getStringList("players." + uuid + ".invites")) {
                                if (name.equals(uuid3)) {
                                    try {
                                        String uuid2 = name;
                                        UUID uuid4 = UUID.fromString(uuid2);
                                        List<String> friends = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".friends"));
                                        List<String> friends1 = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid2 + ".friends"));
                                        List<String> requests = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid2 + ".requests"));
                                        friends.add(uuid2);
                                        friends1.add(uuid);
                                        invites.remove(uuid2);
                                        requests.remove(uuid);
                                        Player targ = Bukkit.getPlayer(uuid4);
                                        Main.data.getConfig().set("players." + uuid2 + ".requests", invites);
                                        Main.data.getConfig().set("players." + uuid + ".invites", requests);
                                        Main.data.saveConfig();
                                        p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You accepted the friend request from " + ChatColor.RED + targ.getName());
                                        targ.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "Your friend request to " + ChatColor.RED + p.getName() + ChatColor.GRAY + " was accepted");
                                        Main.data.getConfig().set("players." + uuid + ".friends", friends);
                                        Main.data.getConfig().set("players." + uuid2 + ".friends", friends1);
                                        Main.data.saveConfig();
                                        return true;
                                    } catch (Exception e) {
                                        String uuid2 = name;
                                        List<String> friends = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".friends"));
                                        List<String> friends1 = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid2 + ".friends"));
                                        List<String> requests = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid2 + ".requests"));
                                        friends.add(uuid2);
                                        friends1.add(uuid);
                                        invites.remove(uuid2);
                                        requests.remove(uuid);
                                        Main.data.getConfig().set("players." + uuid2 + ".requests", invites);
                                        Main.data.getConfig().set("players." + uuid + ".invites", requests);
                                        Main.data.saveConfig();
                                        p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You accepted the friend request from " + ChatColor.RED + args[1]);
                                        List<String> accepted = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid2 + ".accepted"));
                                        accepted.add(p.getName());
                                        Main.data.getConfig().set("players." + uuid2 + ".accepted", accepted);
                                        Main.data.getConfig().set("players." + uuid + ".friends", friends);
                                        Main.data.getConfig().set("players." + uuid2 + ".friends", friends1);
                                        Main.data.saveConfig();
                                        return true;
                                    }
                                }
                            }
                            p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You don't have a friend request from " + ChatColor.RED + args[1]);
                            return true;
                        }
                    }
                }
                else {
                    String uuid = p.getUniqueId().toString();
                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.STRIKETHROUGH + "=======================================");
                    p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD +  "Friends List:");
                    p.sendMessage(ChatColor.WHITE + " ");
                    for (String s : Main.data.getConfig().getStringList("players." + uuid + ".friends")) {
                        OfflinePlayer p2 = Bukkit.getOfflinePlayer(UUID.fromString(s));
                        String name = p2.getName();
                        if (p2.isOnline()) {
                            p.sendMessage(name + ChatColor.GREEN + " - ONLINE");
                        }
                        else {
                            p.sendMessage(name + ChatColor.RED + " - OFFLINE");
                        }
                    }
                    p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.STRIKETHROUGH + "=======================================");
                }
            }
            else {
                sender.sendMessage("Only players may execute this command.");
                return true;
            }
        }
        else if (label.equalsIgnoreCase("unfriend")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!(p.hasPermission(Main.getInstance().getConfig().getString("friendPermission")))) {
                    p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You do not have permission to execute this command.");
                    return true;
                }
                if (args.length == 0) {
                    p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "Please enter the person you would like to unfriend.");
                    return true;
                }
                else {
                    String uuid = p.getUniqueId().toString();
                    String uuid3 = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
                    for (String name : Main.data.getConfig().getStringList("players." + uuid + ".friends")) {
                        if (name.equals(uuid3)) {
                            try {
                                String uuid2 = name;
                                UUID uuid4 = UUID.fromString(uuid2);
                                List<String> friends = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".friends"));
                                List<String> friends1 = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid2 + ".friends"));
                                friends.remove(uuid2);
                                friends1.remove(uuid);
                                Player targ = Bukkit.getPlayer(uuid4);
                                p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You unfriended " + ChatColor.RED + targ.getName());
                                targ.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "Your friend " + ChatColor.RED + p.getName() + ChatColor.GRAY + " removed you from their friends list");
                                Main.data.getConfig().set("players." + uuid + ".friends", friends);
                                Main.data.getConfig().set("players." + uuid2 + ".friends", friends1);
                                Main.data.saveConfig();
                                return true;
                            } catch (Exception e) {
                                String uuid2 = name;
                                List<String> friend = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".friends"));
                                friend.remove(uuid2);
                                List<String> friend1 = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid2 + ".friends"));
                                p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You unfriended " + ChatColor.RED + args[0]);
                                List<String> unfriend = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid2 + ".unfriend"));
                                unfriend.add(p.getName());
                                Main.data.getConfig().set("players." + uuid2 + ".unfriend", unfriend);
                                Main.data.getConfig().set("players." + uuid + ".friends", friend);
                                Main.data.getConfig().set("players." + uuid2 + ".friends", friend1);
                                Main.data.saveConfig();
                                return true;
                            }
                        }
                    }
                    p.sendMessage(ChatColor.RED + "Friends > " + ChatColor.GRAY + "You do not have a friend named " + ChatColor.RED + args[0]);
                    return true;
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("freload")) {
            if (sender.hasPermission("friend.admin")) {
                Main.data.reloadConfig();
                sender.sendMessage(ChatColor.RED + "Reload > " + ChatColor.GRAY + "Config reloaded.");
            }
            else {
                sender.sendMessage(ChatColor.RED + "Reload > " + ChatColor.GRAY + "You do not have permission to execute this command.");
            }
        }
        return false;
    }


}
