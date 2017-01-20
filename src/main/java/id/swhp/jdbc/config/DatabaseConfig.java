package id.swhp.jdbc.config;

import org.apache.commons.dbcp.BasicDataSource;

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
    private static final DatabaseConfig INSTANCE = new DatabaseConfig();
    // database constants
    private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/demo";
    private static final String USERNAME = "demo";
    private static final String PASSWORD = "demo";
    // init DataSource object
    private static final BasicDataSource DATA_SOURCE;

    private DatabaseConfig() {
        // Private Constructor in order use Singleton Pattern
    }

    static {
        LOGGER.log(Level.FINER, "Create Connection to Database");
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DATABASE_DRIVER);
        DATA_SOURCE.setUrl(DATABASE_URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
        try {
            DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public static DatabaseConfig getInstance() {
        return INSTANCE;
    }

    public BasicDataSource getDataSource() {
        return DATA_SOURCE;
    }

    // readResolve method to preserve singleton property
    private Object readResolve() {
        return INSTANCE;
    }
}
