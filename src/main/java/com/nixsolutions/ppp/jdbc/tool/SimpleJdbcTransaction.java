package com.nixsolutions.ppp.jdbc.tool;

import com.nixsolutions.ppp.jdbc.exception.CustomSqlException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

class SimpleJdbcTransaction implements Transaction {
    private final Connection connection;
    private boolean begun;

    public SimpleJdbcTransaction(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public void begin() throws CustomSqlException {
        if (!wasBegun()) {
            try {
                connection.setAutoCommit(false);
                begun = true;
            } catch (SQLException exception) {
                throw new CustomSqlException(exception);
            }
        }
    }

    @Override
    public void commit() throws CustomSqlException {
        try {
            connection.commit();
        } catch (SQLException exception) {
            throw new CustomSqlException(exception);
        }
    }

    @Override
    public void rollback() throws CustomSqlException {
        try {
            connection.rollback();
        } catch (SQLException exception) {
            throw new CustomSqlException(exception);
        }
    }

    @Override
    public void close() throws CustomSqlException {
        if (wasBegun()) {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException exception) {
                throw new CustomSqlException(exception);
            }
        }
    }

    @Override
    public boolean wasBegun() {
        return begun;
    }
}
