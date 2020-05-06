package me.saltyfishhy.friendSystem;

import com.mysql.jdbc.Connection;
import me.saltyfishhy.friendSystem.Commands.Friend;
import me.saltyfishhy.friendSystem.Commands.R;
import me.saltyfishhy.friendSystem.Events.onJoin;
import me.saltyfishhy.friendSystem.FileManager.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.DriverManager;
import java.sql.SQLException;

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
        getCommand("freload").setExecutor(new Friend());
        //SQLSetup();
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

   /* private Connection connection;
    public String host, database, username, password, table;
    public int port;

    public void SQLSetup() {   (SQL Support Coming at a later time)
        if (!Main.getInstance().getConfig().getBoolean("useMySQL")) {
            return;
        }
        else {
            host = Main.getInstance().getConfig().getString("SQLHost");
            port = Main.getInstance().getConfig().getInt("SQLPort");
            database = Main.getInstance().getConfig().getString("SQLDatabase");
            username = Main.getInstance().getConfig().getString("SQLUsername");
            password = Main.getInstance().getConfig().getString("SQLPassword");
            table = Main.getInstance().getConfig().getString("SQLTable");

            try {

                synchronized (this) {
                    if (getConnection() != null && !getConnection().isClosed()) {
                        return;
                    }

                    Class.forName("com.mysql.jdbc.Driver");
                    setConnection((Connection) DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
                    Bukkit.getLogger().info(ChatColor.GREEN + "SQL: Connected!");
                }
            } catch(SQLException e) {
                e.printStackTrace();
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

 */
}
