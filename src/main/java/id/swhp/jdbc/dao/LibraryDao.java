package id.swhp.jdbc.dao;

import id.swhp.jdbc.entity.Author;
import id.swhp.jdbc.entity.BookDetail;
import id.swhp.jdbc.entity.Publisher;

import java.util.List;

public interface LibraryDao {
    public List<BookDetail> findDetailedBook(Integer currentPage, Integer resultRow);

    public BookDetail findDetailedBookById(Integer id);

    public List<BookDetail> findDetailedBookByPublisher(Integer currentPage, Integer resultRow, Publisher publisher);

    public List<BookDetail> findDetailedBookByAuthor(Integer currentPage, Integer resultRow, Author author);
}
