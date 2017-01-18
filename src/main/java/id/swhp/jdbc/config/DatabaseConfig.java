package id.swhp.jdbc.config;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database Configuration using Singleton Pattern
 */
public class DatabaseConfig {
    // logger
    private static final Logger LOGGER = Logger.getLogger(DatabaseConfig.class.getName());
    // instance for DatabaseConfig class
    private static DatabaseConfig instance;
    // database constants
    private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/demo";
    private static final String USERNAME = "demo";
    private static final String PASSWORD = "demo";
    // init DataSource object
    private static BasicDataSource dataSource;

    // Private Constructor in order use Singleton Pattern
    private DatabaseConfig() {
    }

    static {
        LOGGER.log(Level.FINER, "Create Connection to Database");
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DATABASE_DRIVER);
        dataSource.setUrl(DATABASE_URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public BasicDataSource getDataSource() {
        return dataSource;
    }
}
