package id.swhp.jdbc.dao.implementation;

import id.swhp.jdbc.dao.BaseDao;
import id.swhp.jdbc.dao.BasicAction;
import id.swhp.jdbc.entity.Publisher;
import sun.rmi.runtime.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PublisherDaoImpl extends BaseDao<Publisher> implements BasicAction<Publisher, Integer> {
    // logger
    private static final Logger LOGGER = Logger.getLogger(PublisherDaoImpl.class.getName());
    private Connection connection;
    // IMPORTANT: give spaces between SELECT, FROM, and WHERE
    private static final String INSERT = "INSERT INTO publisher(name) VALUES(?)";
    private static final String UPDATE = "UPDATE publisher SET name = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM publisher WHERE id = ?";
    private static final String GET_ALL = "SELECT * FROM publisher LIMIT ?, ?";
    private static final String GET_BY_ID = "SELECT * FROM publisher WHERE id = ?";

    /**
     * Constructor injection
     *
     * @param connection
     */
    public PublisherDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Publisher entity) {
        LOGGER.log(Level.FINER, "Perform Create Data into Database");
        try {
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
                //this.connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    @Override
    public void update(Integer id, Publisher entity) {
        LOGGER.log(Level.FINER, "Perform Update Data in Database");
        try {
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
                //this.connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    @Override
    public void delete(Integer id) {
        LOGGER.log(Level.FINER, "Perform Delete Data in Database");
        try {
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
                //this.connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    @Override
    public List<Publisher> findAll(Integer currentPage, Integer resultRow) {
        LOGGER.log(Level.FINER, "Perform Get All Data in Database");
        List<Publisher> publishers = new ArrayList<Publisher>();
        Integer limit = paginationLimit(currentPage, resultRow);
        try {
            PreparedStatement ps = this.connection.prepareStatement(GET_ALL);

            // index number is same with order of ? in query statements
            ps.setInt(1, limit);
            ps.setInt(2, resultRow);

            ResultSet rs = ps.executeQuery();

            LOGGER.log(Level.FINE, "Start Convert ResultSet into Object");
            while (rs.next()) {
                Publisher publisher = convert(rs);
                publishers.add(publisher);
            }

        } catch (SQLException err) {
            LOGGER.log(Level.WARNING, "Get All Data Error: {0}",
                    new Object[]{err.getMessage()});
        } finally {
            LOGGER.log(Level.FINE, "Get All Data Done");
        }
        return publishers;
    }

    @Override
    public Publisher findById(Integer id) {
        LOGGER.log(Level.FINER, "Perform Get Data by Id in Database");
        Publisher publisher = null;
        try {
            PreparedStatement ps = this.connection.prepareStatement(GET_BY_ID);

            // index number is same with order of ? in query statements
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            LOGGER.log(Level.FINE, "Start Convert ResultSet into Object");
            while (rs.next()) {
                publisher = convert(rs);
            }

        } catch (NullPointerException | SQLException err) {
            LOGGER.log(Level.WARNING, "Get Data by Id Error: {0}",
                    new Object[]{err.getMessage()});
            throw new NullPointerException("Can't Create Publisher Object");
        } finally {
            LOGGER.log(Level.FINE, "Get Data by Id Done");
        }
        return publisher;
    }

    @Override
    public Publisher convert(ResultSet resultSet) throws SQLException {
        LOGGER.log(Level.FINER, "Convert ResultSet into Object");
        Publisher publisher = new Publisher();

        publisher.setId(resultSet.getInt("id"));
        publisher.setName(resultSet.getString("name"));

        return publisher;
    }
}
