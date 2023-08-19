package me.fortibrine.bowspleef.main;

import lombok.Getter;
import me.fortibrine.bowspleef.commands.CommandArena;
import me.fortibrine.bowspleef.listeners.DeathEventListener;
import me.fortibrine.bowspleef.listeners.InventoryListener;
import me.fortibrine.bowspleef.listeners.PlayerLeaveEventListener;
import me.fortibrine.bowspleef.utils.ArenaManager;
import me.fortibrine.bowspleef.utils.SQLManager;
import me.fortibrine.bowspleef.utils.ServerType;
import me.fortibrine.bowspleef.utils.config.MessageConfigUtil;
import me.fortibrine.bowspleef.utils.config.MainConfigUtil;
import me.fortibrine.bowspleef.utils.bungeecord.MessageListener;
import me.fortibrine.bowspleef.utils.bungeecord.MessageSendUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

@Getter
public class Main extends JavaPlugin {

    private MainConfigUtil variableManager;
    private MessageSendUtil messageSendUtil;
    private SQLManager sqlManager;
    private MessageConfigUtil messageConfigUtil;
    private MainConfigUtil mainConfigUtil;
    private ArenaManager arenaManager;

    @Override
    public void onEnable() {

        PluginManager pluginManager = Bukkit.getPluginManager();

        if (!Bukkit.spigot().getConfig().getBoolean("settings.bungeecord")) {
            this.getLogger().log(Level.WARNING, "Сервер не может работать без BungeeCord");
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

    private void initConfigs() {

        File config = new File(this.getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
        }
    }

    @Override
    public void onDisable() {
        if (Bukkit.spigot().getConfig().getBoolean("settings.bungeecord")) {
            Bukkit.getMessenger().unregisterIncomingPluginChannel(this);
            Bukkit.getMessenger().unregisterOutgoingPluginChannel(this);
        }
    }


    private void initUtils() {
        this.variableManager = new MainConfigUtil(this);

        if (variableManager.getServerType() == ServerType.LOBBY) {
            this.sqlManager = new SQLManager(variableManager.getThreads());
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
        pluginManager.registerEvents(new PlayerLeaveEventListener(this), this);
    }

    private void initCommands() {
        if (variableManager.getServerType() == ServerType.LOBBY) {
            this.getCommand("arena").setExecutor(new CommandArena(this));
        }
    }


}
