package me.fortibrine.bowspleef.listeners;

import me.fortibrine.bowspleef.arena.Arena;
import me.fortibrine.bowspleef.main.Main;
import me.fortibrine.bowspleef.utils.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveEventListener implements Listener {

    private ArenaManager arenaManager;
    public PlayerLeaveEventListener(Main plugin) {
        this.arenaManager = plugin.getArenaManager();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        for (Arena arena : this.arenaManager.getArenas()) {
            if (!arena.getPlayers().contains(player.getUniqueId())) return;

            arena.getPlayers().remove(player.getUniqueId());

            if (arena.isInGame()) {
                this.arenaManager.checkWin(arena);
            }

        }
    }

}
