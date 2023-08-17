package me.fortibrine.bowspleef.main;

import lombok.Getter;
import me.fortibrine.bowspleef.listeners.DeathEventListener;
import me.fortibrine.bowspleef.utils.SQLManager;
import me.fortibrine.bowspleef.utils.VariableManager;
import me.fortibrine.bowspleef.utils.bungeecord.MessageListener;
import me.fortibrine.bowspleef.utils.bungeecord.MessageSendUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

@Getter
public class Main extends JavaPlugin {

    private VariableManager variableManager;
    private MessageSendUtil messageSendUtil;
    private SQLManager sqlManager;

    @Override
    public void onEnable() {

        PluginManager pluginManager = Bukkit.getPluginManager();

        if (!Bukkit.spigot().getConfig().getBoolean("settings.bungeecord")) {
            this.getLogger().log(Level.WARNING, "Сервер не может работать без BungeeCord");
            pluginManager.disablePlugin(this);
            return;
        }

        if (Bukkit.spigot().getConfig().getBoolean("settings.bungeecord")) {

            Bukkit.getMessenger().registerIncomingPluginChannel(this, "bowspleef", new MessageListener());
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        }

        File config = new File(this.getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
        }

        this.variableManager = new VariableManager(this);
        this.messageSendUtil = new MessageSendUtil(this);
        this.sqlManager = new SQLManager();

        pluginManager.registerEvents(new DeathEventListener(this), this);

    }

    @Override
    public void onDisable() {
        if (Bukkit.spigot().getConfig().getBoolean("settings.bungeecord")) {
            Bukkit.getMessenger().unregisterIncomingPluginChannel(this);
            Bukkit.getMessenger().unregisterOutgoingPluginChannel(this);
        }
    }


}
