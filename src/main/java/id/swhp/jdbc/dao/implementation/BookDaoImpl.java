package id.swhp.jdbc.dao.implementation;

import id.swhp.jdbc.dao.BaseDao;
import id.swhp.jdbc.dao.BasicAction;
import id.swhp.jdbc.entity.Book;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookDaoImpl extends BaseDao<Book> implements BasicAction<Book, Integer> {
    // logger
    private static final Logger LOGGER = Logger.getLogger(AuthorDaoImpl.class.getName());
    private DataSource dataSource;
    private Connection connection;
    // IMPORTANT: give spaces between SELECT, FROM, and WHERE
    private static final String INSERT = "INSERT INTO book(publisher_id, book_code, title, " +
            "summary, keywords) VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE book SET publisher_id = ?, book_code = ?," +
            "title = ?, summary = ?, keywords = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM book WHERE id = ?";
    private static final String GET_ALL = "SELECT * FROM book LIMIT ?, ?";
    private static final String GET_BY_ID = "SELECT * FROM book WHERE id = ?";

    /**
     * Constructor injection
     *
     * @param dataSource
     */
    public BookDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(Book entity) {
        LOGGER.log(Level.FINER, "Perform Create Data into Database");
        try {
            this.connection = this.dataSource.getConnection();
            this.connection.setAutoCommit(false);

            PreparedStatement ps = this.connection.prepareStatement(INSERT);

            // index number is same with order of ? in query statements
            ps.setInt(1, entity.getPublisherId());
            ps.setString(2, entity.getBookCode());
            ps.setString(3, entity.getTitle());
            ps.setString(4, entity.getSummary());
            ps.setString(5, entity.getKeywords());

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
    public void update(Integer id, Book entity) {
        LOGGER.log(Level.FINER, "Perform Update Data in Database");
        try {
            this.connection = this.dataSource.getConnection();
            this.connection.setAutoCommit(false);

            PreparedStatement ps = this.connection.prepareStatement(UPDATE);

            // index number is same with order of ? in query statements
            ps.setInt(1, entity.getPublisherId());
            ps.setString(2, entity.getBookCode());
            ps.setString(3, entity.getTitle());
            ps.setString(4, entity.getSummary());
            ps.setString(5, entity.getKeywords());
            ps.setInt(6, entity.getId());

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
    public List<Book> findAll(Integer currentPage, Integer resultRow) {
        LOGGER.log(Level.FINER, "Perform Get All Data in Database");
        List<Book> books = new ArrayList<Book>();
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
                Book book = convert(rs);
                books.add(book);
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
        return books;
    }

    @Override
    public Book findById(Integer id) {
        LOGGER.log(Level.FINER, "Perform Get Data by Id in Database");
        Book book = null;
        try {
            this.connection = this.dataSource.getConnection();
            PreparedStatement ps = this.connection.prepareStatement(GET_BY_ID);

            // index number is same with order of ? in query statements
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            LOGGER.log(Level.FINE, "Start Convert ResultSet into Object");
            while (rs.next()) {
                book = convert(rs);
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
        return book;
    }

    @Override
    public Book convert(ResultSet resultSet) throws SQLException {
        LOGGER.log(Level.FINER, "Convert ResultSet into Object");
        Book book = new Book();

        book.setId(resultSet.getInt("id"));
        book.setPublisherId(resultSet.getInt("publisher_id"));
        book.setBookCode(resultSet.getString("book_code"));
        book.setTitle(resultSet.getString("title"));
        book.setSummary(resultSet.getString("summary"));
        book.setKeywords(resultSet.getString("keywords"));

        return book;
    }
}
