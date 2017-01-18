package id.swhp.jdbc.dao.implementation;

import id.swhp.jdbc.dao.BaseDao;
import id.swhp.jdbc.dao.LibraryDao;
import id.swhp.jdbc.entity.Author;
import id.swhp.jdbc.entity.Book;
import id.swhp.jdbc.entity.BookDetail;
import id.swhp.jdbc.entity.Publisher;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibraryDaoImpl extends BaseDao<BookDetail> implements LibraryDao {
    // logger
    private static final Logger LOGGER = Logger.getLogger(AuthorDaoImpl.class.getName());
    private DataSource dataSource;
    private Connection connection;
    // IMPORTANT: give spaces between SELECT, FROM, and WHERE
    private static final String GET_ALL = "SELECT book_detail.id as id, book.id as book_id, book.book_code, " +
            "book.title, book.summary, book.keywords, " +
            "author.id as author_id, author.name as author_name, publisher.id as publisher_id, " +
            "publisher.name as publisher_name " +
            "FROM book_detail " +
            "JOIN author ON author.id = book_detail.author_id " +
            "JOIN book ON book.id = book_detail.book_id " +
            "JOIN publisher ON publisher.id = book.publisher_id " +
            "LIMIT ?, ?";
    private static final String GET_BY_ID = "SELECT book_detail.id as id, book.id as book_id, book.book_code, " +
            "book.title, book.summary, book.keywords, " +
            "author.id as author_id, author.name as author_name, publisher.id as publisher_id, " +
            "publisher.name as publisher_name " +
            "FROM book_detail " +
            "JOIN author ON author.id = book_detail.author_id " +
            "JOIN book ON book.id = book_detail.book_id " +
            "JOIN publisher ON publisher.id = book.publisher_id " +
            "WHERE book_detail.id = ? ";
    private static final String GET_BY_AUTHOR = "SELECT book_detail.id as id, book.id as book_id, book.book_code, " +
            "book.title, book.summary, book.keywords, " +
            "author.id as author_id, author.name as author_name, publisher.id as publisher_id, " +
            "publisher.name as publisher_name " +
            "FROM book_detail " +
            "JOIN author ON author.id = book_detail.author_id " +
            "JOIN book ON book.id = book_detail.book_id " +
            "JOIN publisher ON publisher.id = book.publisher_id " +
            "WHERE author.id = ? " +
            "LIMIT ?, ?";
    private static final String GET_BY_PUBLISHER = "SELECT book_detail.id as id, book.id as book_id, book.book_code, " +
            "book.title, book.summary, book.keywords, " +
            "author.id as author_id, author.name as author_name, publisher.id as publisher_id, " +
            "publisher.name as publisher_name " +
            "FROM book_detail " +
            "JOIN author ON author.id = book_detail.author_id " +
            "JOIN book ON book.id = book_detail.book_id " +
            "JOIN publisher ON publisher.id = book.publisher_id " +
            "WHERE publisher.id = ? " +
            "LIMIT ?, ?";

    /**
     * Constructor injection
     *
     * @param dataSource
     */
    public LibraryDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<BookDetail> findDetailedBook(Integer currentPage, Integer resultRow) {
        LOGGER.log(Level.FINER, "Perform Get All Data in Database");
        List<BookDetail> bookDetails = new ArrayList<BookDetail>();
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
                BookDetail bookDetail = convert(rs);
                bookDetails.add(bookDetail);
            }

        } catch (SQLException err) {
            LOGGER.log(Level.WARNING, "Get All Data Error: {0}",
                    new Object[]{err.getMessage()});
        } finally {
            LOGGER.log(Level.FINE, "Get All Data Done");
            try {
                LOGGER.log(Level.FINE, "Close Connection");
                this.connection.setAutoCommit(true);
                this.connection.close(); // since using ConnectionPolling it will just in SLEEP Mode
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
        return bookDetails;
    }

    @Override
    public BookDetail findDetailedBookById(Integer id) {
        LOGGER.log(Level.FINER, "Perform Get Data by Id in Database");
        BookDetail bookDetail = null;
        try {
            this.connection = this.dataSource.getConnection();
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
            try {
                LOGGER.log(Level.FINE, "Close Connection");
                this.connection.setAutoCommit(true);
                this.connection.close(); // since using ConnectionPolling it will just in SLEEP Mode
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
        return bookDetail;
    }

    @Override
    public List<BookDetail> findDetailedBookByPublisher(Integer currentPage, Integer resultRow, Publisher publisher) {
        LOGGER.log(Level.FINER, "Perform Get All Data in Database");
        List<BookDetail> bookDetails = new ArrayList<BookDetail>();
        Integer limit = paginationLimit(currentPage, resultRow);
        try {
            this.connection = this.dataSource.getConnection();
            PreparedStatement ps = this.connection.prepareStatement(GET_BY_PUBLISHER);

            // index number is same with order of ? in query statements
            ps.setInt(1, publisher.getId());
            ps.setInt(2, limit);
            ps.setInt(3, resultRow);

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
            try {
                LOGGER.log(Level.FINE, "Close Connection");
                this.connection.setAutoCommit(true);
                this.connection.close(); // since using ConnectionPolling it will just in SLEEP Mode
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
        return bookDetails;
    }

    @Override
    public List<BookDetail> findDetailedBookByAuthor(Integer currentPage, Integer resultRow, Author author) {
        LOGGER.log(Level.FINER, "Perform Get All Data in Database");
        List<BookDetail> bookDetails = new ArrayList<BookDetail>();
        Integer limit = paginationLimit(currentPage, resultRow);
        try {
            this.connection = this.dataSource.getConnection();
            PreparedStatement ps = this.connection.prepareStatement(GET_BY_AUTHOR);

            // index number is same with order of ? in query statements
            ps.setInt(1, author.getId());
            ps.setInt(2, limit);
            ps.setInt(3, resultRow);

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
            try {
                LOGGER.log(Level.FINE, "Close Connection");
                this.connection.setAutoCommit(true);
                this.connection.close(); // since using ConnectionPolling it will just in SLEEP Mode
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
        return bookDetails;
    }

    @Override
    public BookDetail convert(ResultSet resultSet) throws SQLException {
        BookDetail bookDetail = new BookDetail();
        Author author = new Author();
        Book book = new Book();
        Publisher publisher = new Publisher();

        author.setName(resultSet.getString("author_name"));
        publisher.setName(resultSet.getString("publisher_name"));
        book.setBookCode(resultSet.getString("book_code"));
        book.setPublisherId(resultSet.getInt("publisher_id"));
        book.setTitle(resultSet.getString("title"));
        book.setSummary(resultSet.getString("summary"));
        book.setKeywords(resultSet.getString("keywords"));
        book.setPublisher(publisher);
        bookDetail.setId(resultSet.getInt("id"));
        bookDetail.setAuthorId(resultSet.getInt("author_id"));
        bookDetail.setBookId(resultSet.getInt("book_id"));
        bookDetail.setAuthor(author);
        bookDetail.setBook(book);

        return bookDetail;
    }
}
