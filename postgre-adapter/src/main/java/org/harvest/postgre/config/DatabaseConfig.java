package org.harvest.postgre.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties properties = loadProperties();

    private static final String DEFAULT_JDBC_URL = "jdbc:postgresql://localhost:5432/harvest?reWriteBatchedInserts=true";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "";
    private static DataSource dataSource;

    public static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = createDataSource();
        }
        return dataSource;
    }

    public static synchronized void setDataSource(DataSource dataSource) {
        DatabaseConfig.dataSource = dataSource;
    }

    public static synchronized void resetDataSource() {
        if (dataSource != null && dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
        dataSource = null;
    }

    private static DataSource createDataSource() {
        HikariConfig config = new HikariConfig();

        // Get configuration values
        String jdbcUrl = getConfigValue(
                "db.jdbc.url",           // System property
                "DB_JDBC_URL",           // Environment variable
                "database.jdbc.url",     // Properties file
                DEFAULT_JDBC_URL         // Default
        );

        String username = getConfigValue(
                "db.username",
                "DB_USERNAME",
                "database.username",
                DEFAULT_USERNAME
        );

        String password = getConfigValue(
                "db.password",
                "DB_PASSWORD",
                "database.password",
                DEFAULT_PASSWORD
        );

        int maxPoolSize = getIntConfigValue(
                "db.pool.max.size",
                "DB_POOL_MAX_SIZE",
                "database.pool.max.size",
                "10"
        );

        int minPoolSize = getIntConfigValue(
                "db.pool.min.size",
                "DB_POOL_MIN_SIZE",
                "database.pool.min.size",
                "2"
        );

        long connectionTimeout = getLongConfigValue(
                "db.connection.timeout",
                "DB_CONNECTION_TIMEOUT",
                "database.connection.timeout",
                "30000"
        );

        long idleTimeout = getLongConfigValue(
                "db.idle.timeout",
                "DB_IDLE_TIMEOUT",
                "database.idle.timeout",
                "600000"
        );

        long maxLifetime = getLongConfigValue(
                "db.max.lifetime",
                "DB_MAX_LIFETIME",
                "database.max.lifetime",
                "1800000"
        );

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minPoolSize);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setPoolName("CockroachDB-Pool");
        config.setDriverClassName("org.postgresql.Driver");

        // Additional PostgreSQL/CockroachDB specific settings
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");

        return new HikariDataSource(config);
    }

    private static String getConfigValue(String... keys) {
        for (String key : keys) {
            // Skip empty keys
            if (key == null || key.trim().isEmpty()) {
                continue;
            }

            // Try system property
            String value = System.getProperty(key);
            if (value != null && !value.trim().isEmpty()) {
                return value;
            }

            // Try environment variable
            value = System.getenv(key);
            if (value != null && !value.trim().isEmpty()) {
                return value;
            }

            // Try properties file
            if (properties != null) {
                value = properties.getProperty(key);
                if (value != null && !value.trim().isEmpty()) {
                    return value;
                }
            }
        }
        return keys[keys.length - 1]; // Return default
    }

    private static int getIntConfigValue(String... keys) {
        String value = getConfigValue(keys);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return Integer.parseInt(keys[keys.length - 1]); // Return default
        }
    }

    private static long getLongConfigValue(String... keys) {
        String value = getConfigValue(keys);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return Long.parseLong(keys[keys.length - 1]); // Return default
        }
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        String configFile = System.getProperty("db.config.file", "database.properties");

        try (InputStream is = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream(configFile)) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            // Use defaults - this is expected in many cases
        }

        return props;
    }

    // For testing purposes
    public static String getJdbcUrl() {
        return getConfigValue(
                "db.jdbc.url",
                "DB_JDBC_URL",
                "database.jdbc.url",
                DEFAULT_JDBC_URL
        );
    }

    public static String getUsername() {
        return getConfigValue(
                "db.username",
                "DB_USERNAME",
                "database.username",
                DEFAULT_USERNAME
        );
    }

    public static String getPassword() {
        return getConfigValue(
                "db.password",
                "DB_PASSWORD",
                "database.password",
                DEFAULT_PASSWORD
        );
    }
}
