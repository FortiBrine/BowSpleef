package me.fortibrine.bowspleef.listeners;

import me.fortibrine.bowspleef.main.Main;
import me.fortibrine.bowspleef.utils.ServerType;
import me.fortibrine.bowspleef.utils.bungeecord.MessageSendUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEventListener implements Listener {

    private MessageSendUtil messageSendUtil;
    private ServerType serverType;
    public DeathEventListener(Main plugin) {
        this.messageSendUtil = plugin.getMessageSendUtil();
        this.serverType = plugin.getVariableManager().getServerType();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (serverType != ServerType.ARENA) return;
        Player player = event.getEntity();

    }

}
