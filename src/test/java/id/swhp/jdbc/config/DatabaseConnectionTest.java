package id.swhp.jdbc.config;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

public class DatabaseConnectionTest {
    private static final String QUERY = "SELECT id FROM author WHERE id = ?";
    private Connection connection;

    @Before
    public void setUp(){
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Test
    public void shouldConnect(){
        assertThat(this.connection, is(not(nullValue())));
    }

    @Test
    public void shouldGetTheCorrectData() {
        try {
            PreparedStatement ps = this.connection.prepareStatement(QUERY);
            ps.setInt(1, 1);
            ResultSet rs = ps.executeQuery();

            assertThat(rs, is(not(nullValue())));
            assertThat(rs.getObject("id"), is(1));
        } catch (SQLException err) {

        }
    }
}
