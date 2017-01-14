package id.swhp.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDao<T> {

    /**
     * Calculate limit of data when fetch using query into database
     *
     * @param currentPage
     * @param resultRow
     * @return Integer
     */
    protected Integer paginationLimit(Integer currentPage, Integer resultRow) {
        int result = 0;
        if (currentPage > 1) {
            result = (currentPage) * resultRow;
        }
        return result;
    }

    /**
     * Convert from ResultSet into Object<T>
     *
     * @param resultSet
     * @return Object<T>
     * @throws SQLException
     */
    public abstract T convert(ResultSet resultSet) throws SQLException;
}
