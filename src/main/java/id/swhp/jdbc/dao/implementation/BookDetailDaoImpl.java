package id.swhp.jdbc.dao.implementation;

import id.swhp.jdbc.dao.BaseDao;
import id.swhp.jdbc.dao.BasicAction;
import id.swhp.jdbc.entity.BookDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookDetailDaoImpl extends BaseDao<BookDetail>
        implements BasicAction<BookDetail, Integer> {
    // logger
    private static final Logger LOGGER = Logger.getLogger(AuthorDaoImpl.class.getName());
    private Connection connection;
    // IMPORTANT: give spaces between SELECT, FROM, and WHERE
    private static final String INSERT = "INSERT INTO book_detail(book_id, author_id) " +
            "VALUES(?, ?)";
    private static final String UPDATE = "UPDATE book_detail SET book_id = ?, " +
            "author_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM book_detail WHERE id = ?";
    private static final String GET_ALL = "SELECT * FROM book_detail LIMIT ?, ?";
    private static final String GET_BY_ID = "SELECT * FROM book_detail WHERE id = ?";

    /**
     * Constructor injection
     *
     * @param connection
     */
    public BookDetailDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(BookDetail entity) {
        LOGGER.log(Level.FINER, "Perform Create Data into Database");
        try {
            this.connection.setAutoCommit(false);

            PreparedStatement ps = this.connection.prepareStatement(INSERT);

            // index number is same with order of ? in query statements
            ps.setInt(1, entity.getBookId());
            ps.setInt(2, entity.getAuthorId());

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
    public void update(Integer id, BookDetail entity) {
        LOGGER.log(Level.FINER, "Perform Update Data in Database");
        try {
            this.connection.setAutoCommit(false);

            PreparedStatement ps = this.connection.prepareStatement(UPDATE);

            // index number is same with order of ? in query statements
            ps.setInt(1, entity.getBookId());
            ps.setInt(2, entity.getAuthorId());
            ps.setInt(3, entity.getId());

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
    public List<BookDetail> findAll(Integer currentPage, Integer resultRow) {
        LOGGER.log(Level.FINER, "Perform Get All Data in Database");
        List<BookDetail> bookDetails = new ArrayList<BookDetail>();
        Integer limit = paginationLimit(currentPage, resultRow);
        try {
            PreparedStatement ps = this.connection.prepareStatement(GET_ALL);

            // index number is same with order of ? in query statements
            ps.setInt(1, limit);
            ps.setInt(2, resultRow);

            ResultSet rs = ps.executeQuery();

            LOGGER.log(Level.FINE, "Start Convert ResultSet into Object");
            while (rs.next()) {
                BookDetail bookDetail = convert(rs);
                bookDetails.add(bookDetail);
            }

        } catch (SQLException err) {
            LOGGER.log(Level.WARNING, "Get All Data Error: {0}",
                    new Object[]{err.getMessage()});
        } finally {
            LOGGER.log(Level.FINE, "Get All Data Done");
        }
        return bookDetails;
    }

    @Override
    public BookDetail findById(Integer id) {
        LOGGER.log(Level.FINER, "Perform Get Data by Id in Database");
        BookDetail bookDetail = null;
        try {
            PreparedStatement ps = this.connection.prepareStatement(GET_BY_ID);

            // index number is same with order of ? in query statements
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            LOGGER.log(Level.FINE, "Start Convert ResultSet into Object");
            while (rs.next()) {
                bookDetail = convert(rs);
            }

        } catch (NullPointerException | SQLException err) {
            LOGGER.log(Level.WARNING, "Get Data by Id Error: {0}",
                    new Object[]{err.getMessage()});
            throw new NullPointerException("Can't Create Publisher Object");
        } finally {
            LOGGER.log(Level.FINE, "Get Data by Id Done");
        }
        return bookDetail;
    }

    @Override
    public BookDetail convert(ResultSet resultSet) throws SQLException {
        LOGGER.log(Level.FINER, "Convert ResultSet into Object");
        BookDetail bookDetail = new BookDetail();

        bookDetail.setId(resultSet.getInt("id"));
        bookDetail.setBookId(resultSet.getInt("book_id"));
        bookDetail.setAuthorId(resultSet.getInt("author_id"));

        return bookDetail;
    }
}
