package me.fortibrine.bowspleef.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SQLManager implements Closeable {

    private HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;
    private Connection connection;
    private ExecutorService executor;

    public SQLManager(int threads) {
        try {
            config.setJdbcUrl("jdbc:sqlite:plugins/BowSpleef/statistic.db");
            dataSource = new HikariDataSource(config);
            connection = dataSource.getConnection();

            executor = Executors.newFixedThreadPool(threads);

            this.initTables();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void initTables() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE IF NOT EXISTS wins(uuid TEXT PRIMARY KET, wins INTEGER)");

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public int getWins(UUID uuid) {
        try {
            return executor.submit(() -> {
                int wins;
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT wins FROM wins WHERE uuid = ?");
                    preparedStatement.setString(1, uuid.toString());

                    ResultSet resultSet = preparedStatement.executeQuery();
                    wins = resultSet.getInt("wins");

                    resultSet.close();
                    preparedStatement.close();
                } catch (SQLException e) {
                    wins = 0;
                    e.printStackTrace();
                }
                return wins;

            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setWins(UUID uuid, int wins) {
        executor.submit(() -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO wins(uuid, wins) VALUES (?,?)");
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setInt(2, wins);

                preparedStatement.executeUpdate();

                preparedStatement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public void close() {
        try {
            connection.close();
            dataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
