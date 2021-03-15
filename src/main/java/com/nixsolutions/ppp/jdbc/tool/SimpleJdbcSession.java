package com.nixsolutions.ppp.jdbc.tool;

import com.nixsolutions.ppp.jdbc.exception.CustomSqlException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Serhii Nesterov
 */
class SimpleJdbcSession implements Session {

    /**
     * This {@code String} contains the query to verify whether or not any row exists.
     * It contains 3 values to be filled: a table name, a column name
     * and its value, limiting a number of queries up to 1 to advance productivity
     */
    private final static String EXISTS_BY_FIELD = "SELECT 1 FROM %s WHERE %s = ? LIMIT 1";

    private final Connection connection;

    private final Transaction transaction;

    /**
     * Constructs a new session with the {@code connection} and {@code transaction}
     *
     * @param connection the connection to work with the database
     * @param transaction the transaction within which queries are executed
     * @throws NullPointerException if the {@code connection} or {@code transaction} is null
     */
    public SimpleJdbcSession(Connection connection, Transaction transaction) {
        this.connection = Objects.requireNonNull(connection);
        this.transaction = Objects.requireNonNull(transaction);
    }

    @Override
    public Transaction beginTransaction() {
        transaction.begin();
        return transaction;
    }

    @Override
    public boolean exists(String table, String field, Object value) {
        Objects.requireNonNull(table);
        Objects.requireNonNull(field);
        String query = String.format(EXISTS_BY_FIELD, table, field);
        return queryForBoolean(query, (resultSet, rowNumber) -> resultSet.next(), value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> queryForObject(String query, RowMapper<T> mapper, Object... params) {
        return (Optional<T>) query(query, mapper, composeQueryHandlerForObject(), params);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> queryForList(String query, RowMapper<T> mapper,
            Object... params) {
        return (List<T>) query(query, mapper, composeQueryHandlerForList(), params);
    }

    @Override
    public void execute(String query, Object... params) {
        Objects.requireNonNull(query);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            fillPreparedStatement(statement, params);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new CustomSqlException(exception);
        }
    }

    @Override
    public void execute(String query, PreparedStatementSetter setter) {
        Objects.requireNonNull(query);
        Objects.requireNonNull(setter);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setter.setValues(statement);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new CustomSqlException(exception);
        }
    }

    @Override
    public void close() {
        transaction.close();
        try {
            connection.close();
        } catch (SQLException exception) {
            throw new CustomSqlException(exception);
        }
    }

    private <T> QueryHandler<T> composeQueryHandlerForObject() {
        return (resultSet, mapper) -> {
            if (resultSet.next()) {
                return Optional.of(mapper.mapRow(resultSet, 1));
            }
            return Optional.empty();
        };
    }

    private <T> QueryHandler<T> composeQueryHandlerForList() {
        return (resultSet, mapper) -> {
            Collection<T> result = new ArrayList<>();
            for (int i = 1; resultSet.next(); i++) {
                result.add(mapper.mapRow(resultSet, i));
            }
            return result;
        };
    }

    private <T> Object query(String query, RowMapper<T> mapper,
            QueryHandler<T> queryHandler, Object... params) {
        Objects.requireNonNull(query);
        Objects.requireNonNull(mapper);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            fillPreparedStatement(statement, params);
            try (ResultSet resultSet = statement.executeQuery()) {
                return queryHandler.apply(resultSet, mapper); // handle results
            }
        } catch (SQLException exception) {
            throw new CustomSqlException(exception);
        }
    }

    private boolean queryForBoolean(String query, RowMapper<Boolean> mapper, Object... params) {
        return queryForObject(query, mapper, params).isPresent();
    }

    private void fillPreparedStatement(PreparedStatement statement,
            Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}
