package com.nixsolutions.ppp.jdbc.tool;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This interface is used to put values into the {@code PreparedStatement}. This
 * can be useful if there are lots of params to be put into
 *
 * @author Serhii Nesterov
 */
@FunctionalInterface
public interface PreparedStatementSetter {

    /**
     * Puts values into the {@code statement}. They must be strictly in order
     *
     * @param statement the statement to put the values into
     * @throws SQLException if any SQL error occurs
     */
    void setValues(PreparedStatement statement) throws SQLException;
}
