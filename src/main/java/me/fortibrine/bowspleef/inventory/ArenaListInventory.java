package me.fortibrine.bowspleef.inventory;

import lombok.Getter;
import me.fortibrine.bowspleef.arena.Arena;
import me.fortibrine.bowspleef.main.Main;
import me.fortibrine.bowspleef.utils.config.MessageConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class ArenaListInventory implements InventoryHolder {

    @Getter
    private Inventory inventory;

    public ArenaListInventory(Main plugin) {
        MessageConfigUtil messageConfigUtil = plugin.getMessageConfigUtil();
        FileConfiguration config = plugin.getConfig();

        List<Arena> arenaList = plugin.getVariableManager().getArenas().stream().filter(Arena::isInGame).collect(Collectors.toList());
        inventory = Bukkit.createInventory(this, 54, messageConfigUtil.supportColorsHEX(config.getString("arena-list.title")).replace("&", "§"));

        arenaList.forEach(arena -> {
            ItemStack item = new ItemStack(Material.matchMaterial(config.getString("arena-list.item.material")));
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(messageConfigUtil.supportColorsHEX(config.getString("arena-list.item.name")).replace("&", "§"));

            List<String> lore = config.getStringList("arena-list.item.lore");
            lore.replaceAll(s -> messageConfigUtil.supportColorsHEX(s).replace("&", "§"));
            meta.setLore(lore);

            item.setItemMeta(meta);
        });
    }

    public void onInventoryClick(InventoryClickEvent event) {

    }

}
