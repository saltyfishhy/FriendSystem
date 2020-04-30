package me.saltyfishhy.friendSystem.Events;

import me.saltyfishhy.friendSystem.FileManager.FileManager;
import me.saltyfishhy.friendSystem.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class onJoin implements Listener {

    public FileManager data;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String uuid = p.getUniqueId().toString();
        if (!(Main.data.getConfig().contains("players." + uuid))) {
            Main.data.getConfig().createSection("players." + uuid);
            Main.data.getConfig().createSection("players." + uuid + ".friends");
            Main.data.getConfig().createSection("players." + uuid + ".requests");
            Main.data.getConfig().createSection("players." + uuid + ".invites");
            Main.data.getConfig().createSection("players." + uuid + ".accepted");
            Main.data.getConfig().createSection("players." + uuid + ".denied");
            Main.data.getConfig().createSection("players." + uuid + ".unfriend");
            Main.data.saveConfig();
        }
        List<String> accepted = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".accepted"));
        List<String> temp = new ArrayList<>(accepted);
        if (accepted.size() > 0) {
            for (String name : accepted) {
                p.sendMessage(ChatColor.RED + "Friend > " + ChatColor.GRAY + "Your friend request to " + ChatColor.RED + name + ChatColor.GRAY + " was accepted.");
                temp.remove(name);
            }
            Main.data.getConfig().set("players." + uuid + ".accepted", temp);
            Main.data.saveConfig();
        }
        List<String> denied = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".denied"));
        List<String> temp2 = new ArrayList<>(denied);
        if (denied.size() > 0) {
            for (String name : denied) {
                p.sendMessage(ChatColor.RED + "Friend > " + ChatColor.GRAY + "Your friend request to " + ChatColor.RED + name + ChatColor.GRAY + " was denied.");
                temp2.remove(name);
            }
            Main.data.getConfig().set("players." + uuid + ".denied", temp2);
            Main.data.saveConfig();
        }
        List<String> unfriended = new ArrayList<>(Main.data.getConfig().getStringList("players." + uuid + ".unfriend"));
        List<String> temp3 = new ArrayList<>(denied);
        if (unfriended.size() > 0) {
            for (String name : unfriended) {
                p.sendMessage(ChatColor.RED + "Friend > " + ChatColor.GRAY + "Your friend " + ChatColor.RED + name + ChatColor.GRAY + " remove you from their friends list.");
                temp3.remove(name);
            }
            Main.data.getConfig().set("players." + uuid + ".unfriend", temp3);
            Main.data.saveConfig();
        }
    }

}
