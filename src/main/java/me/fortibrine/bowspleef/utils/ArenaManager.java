package me.fortibrine.bowspleef.utils;

import lombok.Getter;
import me.fortibrine.bowspleef.arena.Arena;
import me.fortibrine.bowspleef.main.Main;
import me.fortibrine.bowspleef.utils.bungeecord.MessageSendUtil;
import me.fortibrine.bowspleef.utils.config.MainConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ArenaManager {

    @Getter
    private Set<Arena> arenas = new HashSet<>();

    private MainConfigUtil mainConfigUtil;
    private MessageSendUtil messageSendUtil;

    public ArenaManager(Main plugin) {
        FileConfiguration config = plugin.getConfig();
        for (String key : config.getConfigurationSection("servers").getKeys(false)) {
            ConfigurationSection server = config.getConfigurationSection("servers." + key);

            String name = server.getString("name");
            String serverName = server.getString("server");
            int needPlayers = server.getInt("need");
            int maxPlayers = server.getInt("max");

            Arena arena = new Arena(name, serverName, needPlayers, maxPlayers);

            arenas.add(arena);
        }

        this.mainConfigUtil = plugin.getMainConfigUtil();
        this.messageSendUtil = plugin.getMessageSendUtil();
    }

    public void play(Arena arena) {
        arena.setInGame(true);
        arena.getPlayers().forEach(player -> messageSendUtil.connectPlayer(Bukkit.getPlayer(player), arena.getServer()));
    }

    public void checkWin(Arena arena) {
        if (arena.getPlayers().size() != 1) return;
        Player winner = Bukkit.getPlayer(arena.getPlayers().toArray(new UUID[0])[0]);

        this.messageSendUtil.connectPlayer(winner, this.mainConfigUtil.getLobbyServer());
        this.messageSendUtil.addWin(winner);
    }

}
