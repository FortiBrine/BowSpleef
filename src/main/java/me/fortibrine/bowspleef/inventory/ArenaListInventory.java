package me.fortibrine.bowspleef.inventory;

import lombok.Getter;
import me.fortibrine.bowspleef.arena.Arena;
import me.fortibrine.bowspleef.main.Main;
import me.fortibrine.bowspleef.arena.ArenaManager;
import me.fortibrine.bowspleef.utils.config.MessageConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArenaListInventory implements InventoryHolder {

    @Getter
    private Inventory inventory;

    private Map<ItemStack, Arena> itemArena = new HashMap<>();

    private ArenaManager arenaManager;
    private MessageConfigUtil messageConfigUtil;

    public ArenaListInventory(Main plugin) {
        FileConfiguration config = plugin.getConfig();

        this.messageConfigUtil = plugin.getMessageConfigUtil();
        this.arenaManager = plugin.getArenaManager();

        List<Arena> arenaList = arenaManager.getArenas().stream().filter(Arena::isInGame).collect(Collectors.toList());
        inventory = Bukkit.createInventory(this, 54, messageConfigUtil.supportColorsHEX(config.getString("arena-list.title")).replace("&", "§"));

        arenaList.forEach(arena -> {
            ItemStack item = new ItemStack(Material.matchMaterial(config.getString("arena-list.item.material")));
            ItemMeta meta = item.getItemMeta();

            String displayName = messageConfigUtil.supportColorsHEX(config.getString("arena-list.item.name")).replace("&", "§");
            meta.setDisplayName(arena.insertArena(displayName));

            List<String> lore = config.getStringList("arena-list.item.lore");
            lore.replaceAll(s -> messageConfigUtil.supportColorsHEX(s).replace("&", "§"));
            lore.replaceAll(arena::insertArena);
            meta.setLore(lore);

            item.setItemMeta(meta);

            inventory.addItem(item);
            this.itemArena.put(item, arena);
        });
    }

    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;

        Arena arena = itemArena.get(currentItem);
        if (arena.isInGame()) return;

        int players = arena.getPlayers().size() + 1;
        if (players > arena.getMaxPlayers()) return;

        for (Arena arenaInList : arenaManager.getArenas()) {
            if (arenaInList.getPlayers().contains(player.getUniqueId())) {
                arenaInList.getPlayers().remove(player.getUniqueId());
                return;
            }
        }

        arena.getPlayers().add(player.getUniqueId());

        if (players > arena.getNeedPlayers()) {
            arenaManager.play(arena);
        }
    }

}
