package me.fortibrine.bowspleef.commands;

import me.fortibrine.bowspleef.inventory.ArenaListInventory;
import me.fortibrine.bowspleef.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandArena implements CommandExecutor {

    private Main plugin;
    public CommandArena(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("bowspleef.arena")) {
            player.sendMessage(plugin.getMessageConfigUtil().parseMessage("not-enough-permission"));
            return true;
        }

        ArenaListInventory arenaListInventory = new ArenaListInventory(plugin);
        player.openInventory(arenaListInventory.getInventory());

        return true;
    }
}
