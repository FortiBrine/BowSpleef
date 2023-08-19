package me.fortibrine.bowspleef.utils.config;

import lombok.Getter;
import me.fortibrine.bowspleef.main.Main;
import me.fortibrine.bowspleef.utils.ServerType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@Getter
public class MainConfigUtil {

    private ServerType serverType;
    private int threads;
    private String lobbyServer;

    public MainConfigUtil(Main plugin) {

        FileConfiguration config = plugin.getConfig();

        threads = config.getInt("threads");

        this.serverType = ServerType.valueOf(config.getString("server-type"));
        this.lobbyServer = config.getString("lobby");

        config.getStringList("ignore-events").forEach(eventString -> {
            try {

                Bukkit.getPluginManager().registerEvent(
                        (Class<? extends Event>) Class.forName(eventString),
                        new Listener() {},
                        EventPriority.HIGHEST,
                        (listener, event) -> {
                            if (event instanceof Cancellable) {
                                Cancellable cancellable = (Cancellable) event;
                                cancellable.setCancelled(true);
                            }
                        },
                        plugin
                );

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });
    }

}

