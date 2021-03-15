package com.nixsolutions.ppp.jdbc.tool;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This interface is used to pass a mechanism of how to map values in the
 * {@code resultSet} to the {@code T}
 *
 * @param <T> the type of values to be mapped to
 * @author Serhii Nesterov
 */
@FunctionalInterface
public interface RowMapper<T> {

    /**
     * Maps all the values of the {@code resultSet} to the {@code T}
     *
     * @param resultSet the {@code ResultSet} with the query results
     * @param rowNumber the current row number
     * @return the mapped object
     * @throws SQLException of any SQL error occurs
     */
    T mapRow(ResultSet resultSet, int rowNumber) throws SQLException;
}
