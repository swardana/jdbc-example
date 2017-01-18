package id.swhp.jdbc.dao;

import id.swhp.jdbc.config.DatabaseConfig;
import id.swhp.jdbc.dao.implementation.AuthorDaoImpl;
import id.swhp.jdbc.dao.implementation.LibraryDaoImpl;
import id.swhp.jdbc.dao.implementation.PublisherDaoImpl;
import id.swhp.jdbc.entity.Author;
import id.swhp.jdbc.entity.BookDetail;
import id.swhp.jdbc.entity.Publisher;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;

public class LibraryDaoTest {
    private LibraryDao libraryDao;
    private BasicAction<Author, Integer> authorDao;
    private BasicAction<Publisher, Integer> publisherDao;

    @Before
    public void setUp() {
        DataSource dataSource = DatabaseConfig.getInstance().getDataSource();
        this.libraryDao = new LibraryDaoImpl(dataSource);
        this.authorDao = new AuthorDaoImpl(dataSource);
        this.publisherDao = new PublisherDaoImpl(dataSource);
    }

    @Test
    public void shouldFindAllBook() {
        List<BookDetail> bookDetails = libraryDao.findDetailedBook(0, 5);

        assertThat(bookDetails, is(not(empty())));
    }

    @Test
    public void shouldFindBookById() {
        int id = 1;
        BookDetail bookDetail = libraryDao.findDetailedBookById(id);

        assertThat(bookDetail, is(notNullValue()));
        assertThat(bookDetail.getId(), is(equalTo(id)));
    }

    @Test
    public void shouldFindBookByAuthor(){
        int id = 1;
        Author author = this.authorDao.findById(id);
        List<BookDetail> bookDetails = this.libraryDao.findDetailedBookByAuthor(0, 5, author);

        assertThat(bookDetails.get(0).getAuthorId(), is(equalTo(author.getId())));
    }

    @Test
    public void shouldFindBookByPublisher() {
        int id = 1;
        Publisher publisher = this.publisherDao.findById(id);
        List<BookDetail> bookDetails = this.libraryDao.findDetailedBookByPublisher(0, 5, publisher);

        assertThat(bookDetails.get(0).getBook().getPublisherId(), is(equalTo(publisher.getId())));
    }
}
