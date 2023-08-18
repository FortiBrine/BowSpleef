package me.fortibrine.bowspleef.utils.config;

import lombok.Getter;
import me.fortibrine.bowspleef.arena.Arena;
import me.fortibrine.bowspleef.main.Main;
import me.fortibrine.bowspleef.utils.ServerType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

@Getter
public class VariableManager {

    private List<Arena> arenas = new ArrayList<>();
    private ServerType serverType;
    private int threads;

    public VariableManager(Main plugin) {

        FileConfiguration config = plugin.getConfig();

        threads = config.getInt("threads");

        for (String key : config.getConfigurationSection("servers").getKeys(false)) {
            ConfigurationSection server = config.getConfigurationSection("servers." + key);

            String name = server.getString("name");
            String serverName = server.getString("server");
            int needPlayers = server.getInt("need");
            int maxPlayers = server.getInt("max");

            Arena arena = new Arena(name, serverName, needPlayers, maxPlayers);

            arenas.add(arena);
        }

        this.serverType = ServerType.valueOf(config.getString("server-type"));

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

