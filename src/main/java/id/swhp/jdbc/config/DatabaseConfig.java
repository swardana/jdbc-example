package id.swhp.jdbc.config;

import java.sql.Connection;
import java.sql.DriverManager;
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
    // init connection object
    private static Connection connection;

    private DatabaseConfig() {
    }

    static {
        try {
            LOGGER.log(Level.FINER, "Create Connection to Database");
            Class.forName(DATABASE_DRIVER);
            try {
                connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            } catch (SQLException e){
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        } catch (ClassNotFoundException err) {
            LOGGER.log(Level.SEVERE, err.toString(), err);
        }
    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
