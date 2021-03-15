package com.nixsolutions.ppp.jdbc.tool;

import com.nixsolutions.ppp.jdbc.exception.CustomSqlException;

import java.util.List;
import java.util.Optional;

/**
 * The {@code Session} class is designed for simplification of working with a database.
 * It implements {@code AutoCloseable} interface, therefore, it can be used in the
 * try-with-resources block
 *
 * @author Serhii Nesterov
 */
public interface Session extends AutoCloseable {

    /**
     * Begins a transaction switching auto committing to false.
     * Each of queries is executed in a single transaction after invoking
     * this method
     *
     * @return an instance of {@code Transaction}
     * @throws CustomSqlException if any SQL error occurs
     */
    Transaction beginTransaction() throws CustomSqlException;

    /**
     * Executes the {@code query} (SELECT) to the specified {@code table} filtering by {@code field}
     * and its {@code value}. If there is at least one row, then it returns true;
     * otherwise - false
     *
     * @param table the table to query to
     * @param field the field to be filtered
     * @param value the value to be filtered
     * @return {@code true}, if there is at least one row, otherwise - {@code false}
     * @throws NullPointerException if the {@code table} or {@code field} is null
     * @throws CustomSqlException   if any SQL error occurs
     */
    boolean exists(String table, String field, Object value)
            throws CustomSqlException;

    /**
     * Executes the {@code query} (SELECT) and returns an instance of
     * {@code Optional<T>} with the mapped object. {@code mapper} must be provided
     * in order to map values from {@code ResultSet} into {@code <T>}
     *
     * @param query  the query to be executed (SELECT operation)
     * @param mapper the mapper used to map values from {@code ResultSet} into {@code <T>}
     * @param params the params to be placed in the query (strictly in order)
     * @param <T>    the type to be mapped to
     * @return an instance of the {@code Optional<T>} with the mapped object
     * @throws NullPointerException if the {@code query} or {@code mapper} is null
     * @throws CustomSqlException   if any SQL error occurs
     * @see #queryForList(String, RowMapper, Object...) 
     */
    <T> Optional<T> queryForObject(String query, RowMapper<T> mapper,
            Object... params) throws CustomSqlException;

    /**
     * Executes the {@code query} and returns a {@code List<T>} with the mapped objects.
     * {@code mapper} must be provided in order to map values from {@code ResultSet}
     * into {@code List<T>}
     *
     * @param query  the query to be executed (SELECT operation)
     * @param mapper the mapper used to map values from {@code ResultSet} into {@code List<T>}
     * @param params the params to be placed in the {@code query} (strictly in order)
     * @param <T>    the type to be mapped to
     * @return the {@code List<T>} of the objects fetched by the {@code query}
     * @throws NullPointerException if the {@code query} or {@code mapper} is null
     * @throws CustomSqlException   if any SQL error occurs
     * @see #queryForObject(String, RowMapper, Object...) 
     */
    <T> List<T> queryForList(String query, RowMapper<T> mapper,
            Object... params) throws CustomSqlException;

    /**
     * Executes the {@code query} (INSERT, UPDATE or DELETE) using {@code params}
     *
     * @param query  the query to be executed (INSERT, UPDATE or DELETE operation)
     * @param params the params to be placed in the {@code query} (strictly in order)
     * @throws NullPointerException if the {@code query} is null
     * @throws CustomSqlException   if any SQL error occurs
     * @see #execute(String, PreparedStatementSetter)
     */
    void execute(String query, Object... params) throws CustomSqlException;

    /**
     * Executes the {@code query} (INSERT, UPDATE or DELETE) using {@code setter}.
     * The method is useful when there are lots of params to be put into the query.
     * {@code PreparedStatementSetter} has to be implemented passing all the necessary params
     *
     * @param query  the query to be executed (INSERT, UPDATE or DELETE operation)
     * @param setter the setter used to put params into {@code PreparedStatement}
     * @throws NullPointerException if the {@code query} or {@code setter} is null
     * @throws CustomSqlException   if any SQL error occurs
     * @see #execute(String, Object...)
     */
    void execute(String query, PreparedStatementSetter setter)
            throws CustomSqlException;

    /**
     * This method closes the current transaction and connection (returns it into the pool).
     * Used in the try-with-resources constructions
     *
     * @throws CustomSqlException if any SQL error occurs
     */
    @Override
    void close() throws CustomSqlException;
}
