package id.swhp.jdbc.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = this.connection.prepareStatement(QUERY);
            rs = ps.executeQuery();

            assertThat(rs, is(not(nullValue())));
            assertThat(rs.getObject("id"), is(1));
        } catch (SQLException e) {

        }
    }
}
