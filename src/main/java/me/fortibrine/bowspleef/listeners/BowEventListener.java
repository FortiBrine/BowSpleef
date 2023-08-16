package me.fortibrine.bowspleef.listeners;

import me.fortibrine.bowspleef.main.Main;
import me.fortibrine.bowspleef.utils.ServerType;
import me.fortibrine.bowspleef.utils.VariableManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BowEventListener implements Listener {

    private VariableManager variableManager;
    public BowEventListener(Main plugin) {
        this.variableManager = plugin.getVariableManager();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (this.variableManager.getServerType() != ServerType.ARENA) return;
    }

}
