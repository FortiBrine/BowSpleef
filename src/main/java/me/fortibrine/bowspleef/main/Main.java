package me.fortibrine.bowspleef.main;

import lombok.Getter;
import me.fortibrine.bowspleef.commands.CommandArena;
import me.fortibrine.bowspleef.listeners.DeathEventListener;
import me.fortibrine.bowspleef.listeners.InventoryListener;
import me.fortibrine.bowspleef.listeners.PlayerQuitListener;
import me.fortibrine.bowspleef.arena.ArenaManager;
import me.fortibrine.bowspleef.utils.sql.DatabaseManager;
import me.fortibrine.bowspleef.utils.ServerType;
import me.fortibrine.bowspleef.utils.config.MessageConfigUtil;
import me.fortibrine.bowspleef.utils.config.MainConfigUtil;
import me.fortibrine.bowspleef.utils.bungeecord.MessageListener;
import me.fortibrine.bowspleef.utils.bungeecord.MessageSendUtil;
import me.fortibrine.bowspleef.utils.sql.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class Main extends JavaPlugin {

    private MainConfigUtil variableManager;
    private MessageSendUtil messageSendUtil;
    private DatabaseManager databaseManager;
    private MessageConfigUtil messageConfigUtil;
    private MainConfigUtil mainConfigUtil;
    private ArenaManager arenaManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {

        PluginManager pluginManager = Bukkit.getPluginManager();

        if (!Bukkit.spigot().getConfig().getBoolean("settings.bungeecord")) {
            pluginManager.disablePlugin(this);
            return;
        }

        this.initConfigs();
        this.initUtils();
        this.initListeners();
        this.initCommands();

        if (Bukkit.spigot().getConfig().getBoolean("settings.bungeecord")) {

            Bukkit.getMessenger().registerIncomingPluginChannel(this, "bowspleef", new MessageListener(this));
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        }

    }


    @Override
    public void onDisable() {
        if (Bukkit.spigot().getConfig().getBoolean("settings.bungeecord")) {
            Bukkit.getMessenger().unregisterIncomingPluginChannel(this);
            Bukkit.getMessenger().unregisterOutgoingPluginChannel(this);
            this.databaseManager.close();
        }

    }

    private void initConfigs() {

        File config = new File(this.getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
        }
    }

    private void initUtils() {
        this.variableManager = new MainConfigUtil(this);

        if (variableManager.getServerType() == ServerType.LOBBY) {
            this.databaseManager = new DatabaseManager(variableManager.getThreads());
            this.playerManager = new PlayerManager(this);
        }

        this.messageSendUtil = new MessageSendUtil(this);
        this.messageConfigUtil = new MessageConfigUtil(this);
        this.mainConfigUtil = new MainConfigUtil(this);
        this.arenaManager = new ArenaManager(this);
    }

    private void initListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new DeathEventListener(this), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
    }

    private void initCommands() {
        if (variableManager.getServerType() == ServerType.LOBBY) {
            this.getCommand("arena").setExecutor(new CommandArena(this));
        }
    }


}
