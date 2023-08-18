package me.fortibrine.bowspleef.listeners;

import me.fortibrine.bowspleef.inventory.ArenaListInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;

        Inventory inventory = event.getClickedInventory();
        if (inventory.getHolder() == null) return;

        if (inventory.getHolder() instanceof ArenaListInventory) {
            ((ArenaListInventory) inventory.getHolder()).onInventoryClick(event);
        }
    }

}
