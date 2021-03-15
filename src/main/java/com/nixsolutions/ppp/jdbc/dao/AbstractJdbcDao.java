package com.nixsolutions.ppp.jdbc.dao;

import com.nixsolutions.ppp.jdbc.config.DataSourceFactory;
import com.nixsolutions.ppp.jdbc.exception.CustomSqlException;

import java.sql.Connection;
import java.sql.SQLException;

abstract class AbstractJdbcDao {

    Connection createConnection() {
        try {
            return DataSourceFactory.getInstance().getDataSource().getConnection();
        } catch (SQLException exception) {
            throw new CustomSqlException(exception);
        }
    }
}
