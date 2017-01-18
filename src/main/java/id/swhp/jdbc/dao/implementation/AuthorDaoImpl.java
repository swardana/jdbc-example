package id.swhp.jdbc.dao.implementation;

import id.swhp.jdbc.dao.BaseDao;
import id.swhp.jdbc.dao.BasicAction;
import id.swhp.jdbc.entity.Author;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthorDaoImpl extends BaseDao<Author> implements BasicAction<Author, Integer> {
    // logger
    private static final Logger LOGGER = Logger.getLogger(AuthorDaoImpl.class.getName());
    private DataSource dataSource;
    private Connection connection;
    // IMPORTANT: give spaces between SELECT, FROM, and WHERE
    private static final String INSERT = "INSERT INTO author(name) VALUES(?)";
    private static final String UPDATE = "UPDATE author SET name = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM author WHERE id = ?";
    private static final String GET_ALL = "SELECT * FROM author LIMIT ?, ?";
    private static final String GET_BY_ID = "SELECT * FROM author WHERE id = ?";

    /**
     * Constructor injection
     *
     * @param dataSource
     */
    public AuthorDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(Author entity) {
        LOGGER.log(Level.FINER, "Perform Create Data into Database");

        try {
            this.connection = this.dataSource.getConnection();
            this.connection.setAutoCommit(false);

            PreparedStatement ps = this.connection.prepareStatement(INSERT);

            // index number is same with order of ? in query statements
            ps.setString(1, entity.getName());

            ps.executeUpdate();

            this.connection.commit();
        } catch (Exception err) {
            LOGGER.log(Level.WARNING, "Insert Data Error: {0}",
                    new Object[]{err.getMessage()});
            try {
                LOGGER.log(Level.FINE, "Insert Data Failed! Rollback Everything");
                this.connection.rollback();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        } finally {
            LOGGER.log(Level.FINE, "Insert Data Done");
            try {
                LOGGER.log(Level.FINE, "Close Connection");
                this.connection.setAutoCommit(true);
                this.connection.close(); // since using ConnectionPolling it will just in SLEEP Mode
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    @Override
    public void update(Integer id, Author entity) {
        LOGGER.log(Level.FINER, "Perform Update Data in Database");
        try {
            this.connection = this.dataSource.getConnection();
            this.connection.setAutoCommit(false);

            PreparedStatement ps = this.connection.prepareStatement(UPDATE);

            // index number is same with order of ? in query statements
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getId());

            ps.executeUpdate();

            this.connection.commit();
        } catch (Exception err) {
            LOGGER.log(Level.WARNING, "Update Data Error: {0}",
                    new Object[]{err.getMessage()});
            try {
                LOGGER.log(Level.FINE, "Update Data Failed! Rollback Everything");
                this.connection.rollback();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        } finally {
            LOGGER.log(Level.FINE, "Update Data Done");
            try {
                LOGGER.log(Level.FINE, "Close Connection");
                this.connection.setAutoCommit(true);
                this.connection.close(); // since using ConnectionPolling it will just in SLEEP Mode
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    @Override
    public void delete(Integer id) {
        LOGGER.log(Level.FINER, "Perform Delete Data in Database");
        try {
            this.connection = this.dataSource.getConnection();
            this.connection.setAutoCommit(false);

            PreparedStatement ps = this.connection.prepareStatement(DELETE);

            // index number is same with order of ? in query statements
            ps.setInt(1, id);

            ps.executeUpdate();

            this.connection.commit();
        } catch (Exception err) {
            LOGGER.log(Level.WARNING, "Delete Data Error: {0}",
                    new Object[]{err.getMessage()});
            try {
                LOGGER.log(Level.FINE, "Delete Data Failed! Rollback Everything");
                this.connection.rollback();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        } finally {
            LOGGER.log(Level.FINE, "Delete Data Done");
            try {
                LOGGER.log(Level.FINE, "Close Connection");
                this.connection.setAutoCommit(true);
                this.connection.close(); // since using ConnectionPolling it will just in SLEEP Mode
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    @Override
    public List<Author> findAll(Integer currentPage, Integer resultRow) {
        LOGGER.log(Level.FINER, "Perform Get All Data in Database");
        List<Author> authors = new ArrayList<Author>();
        Integer limit = paginationLimit(currentPage, resultRow);
        try {
            this.connection = this.dataSource.getConnection();
            PreparedStatement ps = this.connection.prepareStatement(GET_ALL);

            // index number is same with order of ? in query statements
            ps.setInt(1, limit);
            ps.setInt(2, resultRow);

            ResultSet rs = ps.executeQuery();

            LOGGER.log(Level.FINE, "Start Convert ResultSet into Object");
            while (rs.next()) {
                Author author = convert(rs);
                authors.add(author);
            }

        } catch (SQLException err) {
            LOGGER.log(Level.WARNING, "Get All Data Error: {0}",
                    new Object[]{err.getMessage()});
        } finally {
            LOGGER.log(Level.FINE, "Get All Data Done");
            try {
                LOGGER.log(Level.FINE, "Close Connection");
                this.connection.close(); // since using ConnectionPolling it will just in SLEEP Mode
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
        return authors;
    }

    @Override
    public Author findById(Integer id) {
        LOGGER.log(Level.FINER, "Perform Get Data by Id in Database");
        Author author = null;
        try {
            this.connection = this.dataSource.getConnection();
            PreparedStatement ps = this.connection.prepareStatement(GET_BY_ID);

            // index number is same with order of ? in query statements
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            LOGGER.log(Level.FINE, "Start Convert ResultSet into Object");
            while (rs.next()) {
                author = convert(rs);
            }

        } catch (NullPointerException | SQLException err) {
            LOGGER.log(Level.WARNING, "Get Data by Id Error: {0}",
                    new Object[]{err.getMessage()});
            throw new NullPointerException("Can't Create Publisher Object");
        } finally {
            LOGGER.log(Level.FINE, "Get Data by Id Done");
            try {
                LOGGER.log(Level.FINE, "Close Connection");
                this.connection.close(); // since using ConnectionPolling it will just in SLEEP Mode
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
        return author;
    }

    @Override
    public Author convert(ResultSet resultSet) throws SQLException {
        LOGGER.log(Level.FINER, "Convert ResultSet into Object");
        Author author = new Author();

        author.setId(resultSet.getInt("id"));
        author.setName(resultSet.getString("name"));

        return author;
    }
}
