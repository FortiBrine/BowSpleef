package me.fortibrine.bowspleef.utils.sql;

import me.fortibrine.bowspleef.main.Main;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public final class PlayerManager {

    private DatabaseManager databaseManager;
    public PlayerManager(Main plugin) {
        this.databaseManager = plugin.getDatabaseManager();

        databaseManager.prepareStatementUpdate("CREATE IF NOT EXISTS wins(uuid TEXT PRIMARY KET, wins INTEGER)");
    }

    public void addWin(Player player) {
        this.setWins(player, this.getWins(player) + 1);
    }

    public void setWins(Player player, int wins) {
        databaseManager.prepareStatement("INSERT INTO wins(uuid, wins) VALUES(?, ?)", player.getUniqueId().toString(), wins);
        databaseManager.prepareStatement("UPDATE wins SET wins = ? WHERE uuid = ?", wins, player.getUniqueId().toString());
    }

    public int getWins(Player player) {
        try {
            ResultSet resultSet = databaseManager.prepareStatement("SELECT wins FROM wins WHERE uuid = ?", player.getUniqueId().toString()).get();

            return resultSet.getInt("wins");
        } catch (InterruptedException | ExecutionException | SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
