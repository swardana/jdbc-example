package id.swhp.jdbc.config;

import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class DatabaseConnectionTest {
    private static final String QUERY = "SELECT id FROM author WHERE id = ?";
    private DataSource dataSource;

    @Before
    public void setUp(){
        this.dataSource = DatabaseConfig.getInstance().getDataSource();
    }

    @Test
    public void shouldConnect() throws SQLException {
        assertThat(this.dataSource.getConnection(), is(not(nullValue())));
    }

    @Test
    public void shouldGetTheCorrectData() {
        Integer id = 1;
        Integer resultId = null;
        try {
            Connection connection = this.dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(QUERY);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            // avoid cursor before the first row
            if(rs.next()) {
                resultId = rs.getInt("id");
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
        assertThat(resultId, is(id));
    }
}
