package com.nixsolutions.ppp.jdbc.tool;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
interface QueryHandler<T> {
    Object apply(ResultSet resultSet, RowMapper<T> mapper) throws SQLException;
}
