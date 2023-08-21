package me.fortibrine.bowspleef.utils.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class DatabaseManager implements Closeable {

    private final HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;
    private Connection connection;
    private ExecutorService executor;

    public DatabaseManager(int threads) {
        try {
            config.setJdbcUrl("jdbc:sqlite:plugins/BowSpleef/statistic.db");
            dataSource = new HikariDataSource(config);
            connection = dataSource.getConnection();

            executor = Executors.newFixedThreadPool(threads);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void prepareStatementUpdate(String query, Object... objects) {
        executor.submit(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                return statement.executeUpdate();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    public Future<ResultSet> prepareStatement(String query, Object... objects) {
        return executor.submit(() -> {
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                return statement.executeQuery();
            } catch (SQLException exception) {
                throw new RuntimeException(exception);
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
