package me.fortibrine.bowspleef.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLManager {

    private HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;
    private Connection connection;

    public SQLManager() {
        try {
            config.setJdbcUrl("jdbc:sqlite:plugins/BowSpleef/statistic.db");
            dataSource = new HikariDataSource(config);
            connection = dataSource.getConnection();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
