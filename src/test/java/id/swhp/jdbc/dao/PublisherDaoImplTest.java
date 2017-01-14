package id.swhp.jdbc.dao;

import id.swhp.jdbc.config.DatabaseConfig;
import id.swhp.jdbc.dao.implementation.PublisherDaoImpl;
import id.swhp.jdbc.entity.Publisher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class PublisherDaoImplTest {
    private static final String DELETE_TABLE = "DELETE FROM publisher";
    private static final String ALTER_INCREMENT = "ALTER TABLE publisher AUTO_INCREMENT = 1";
    private BasicAction<Publisher, Integer> publisherDao;

    @Before
    public void setUp() {
        Publisher publisher = new Publisher();
        publisher.setName("INITIAL DATA");

        Connection connection = DatabaseConfig.getInstance().getConnection();
        this.publisherDao = new PublisherDaoImpl(connection);

        this.publisherDao.create(publisher);
    }

    @After
    public void tearDown() {
        try {
            Connection connection = DatabaseConfig.getInstance().getConnection();

            PreparedStatement delete = connection.prepareStatement(DELETE_TABLE);
            PreparedStatement alter = connection.prepareStatement(ALTER_INCREMENT);

            delete.executeUpdate();
            alter.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldSaveAuthor() {
        String name = "DAILY EXPRESS";
        List<Publisher> publishers = new ArrayList<Publisher>();
        Publisher publisher = new Publisher();

        publisher.setName(name);
        this.publisherDao.create(publisher);

        publishers = this.publisherDao.findAll(0, 5);

        assertThat(publishers, is(not(empty())));
        List<Publisher> checkData = publishers.stream().filter(p -> p.getName().equals(name))
                .collect(Collectors.toList());
        assertThat(checkData, is(not(empty())));
    }

    @Test
    public void shouldUpdateData() {
        String name = "EXPRESS RAILWAY";
        String updateName = "MORNING EXPRESS";
        List<Publisher> publishers = new ArrayList<Publisher>();
        Publisher publisher = new Publisher();

        publisher.setName(name);
        this.publisherDao.create(publisher);

        publishers = this.publisherDao.findAll(0, 5);

        List<Publisher> data = publishers.stream().filter(p -> p.getName().equals(name))
                .collect(Collectors.toList());

        Publisher updateData = data.get(0);
        updateData.setName(updateName);

        this.publisherDao.update(updateData.getId(), updateData);

        List<Publisher> newData = this.publisherDao.findAll(0, 5);

        List<Publisher> checkData = publishers.stream().filter(p -> p.getName().equals(updateName))
                .collect(Collectors.toList());

        assertThat(checkData, is(not(empty())));
        assertThat(checkData.get(0).getName(), is(equalTo(updateName)));
    }

    @Test
    public void shouldDeleteData(){
        Publisher publisher = new Publisher();
        publisher.setName("J.J DAILY");
        List<Publisher> before = new ArrayList<Publisher>();
        List<Publisher> after = new ArrayList<Publisher>();

        this.publisherDao.create(publisher);

        before = this.publisherDao.findAll(0, 5);

        this.publisherDao.delete(before.get(0).getId());

        after = this.publisherDao.findAll(0, 5);

        assertThat(before.size() - 1, is(equalTo(after.size())));
    }

    @Test
    public void shouldGetAllData() {
        List<Publisher> publishers = this.publisherDao.findAll(0, 5);

        assertThat(publishers, is(not(empty())));
    }

    @Test
    public void shouldGetDataById(){
        int id = 1;
        Publisher publisher = this.publisherDao.findById(id);

        assertThat(publisher.getId(), is(id));
    }

    @Test
    public void shouldPaginateData() {
        int currPage = 0;
        int resultRow = 1;
        List<Publisher> publishers = this.publisherDao.findAll(currPage, resultRow);

        assertThat(publishers, hasSize(1));
    }
}
