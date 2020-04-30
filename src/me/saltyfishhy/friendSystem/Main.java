package me.saltyfishhy.friendSystem;

import me.saltyfishhy.friendSystem.Commands.Friend;
import me.saltyfishhy.friendSystem.Commands.R;
import me.saltyfishhy.friendSystem.Events.onJoin;
import me.saltyfishhy.friendSystem.FileManager.FileManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static FileManager data;
    public Listener listener = new onJoin();
    public static Main instance;

    @Override
    public void onEnable() {
        getLogger().info("Friend System Plugin Enabled.");
        this.data = new FileManager(this);
        loadConfig();
        instance = this;
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(listener, this);
        getCommand("friend").setExecutor(new Friend());
        getCommand("unfriend").setExecutor(new Friend());
        getCommand("msg").setExecutor(new R());
        getCommand("reply").setExecutor(new R());
        data.getConfig().options().copyDefaults(true);
        data.saveConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("Friend System Plugin Disabled.");
    }

    public void loadConfig(){
        //tells the config file to copy the defaults that were created with the file initially
        getConfig().options().copyDefaults(true);
        //saves the config
        saveConfig();
    }

    public static Main getInstance() {
        return instance;
    }

}
