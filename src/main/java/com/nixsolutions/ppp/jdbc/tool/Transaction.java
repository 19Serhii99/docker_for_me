package com.nixsolutions.ppp.jdbc.tool;

import com.nixsolutions.ppp.jdbc.exception.CustomSqlException;

/**
 * The {@code Transaction} is used to simplify working with transactions
 *
 * @author Serhii Nesterov
 */
public interface Transaction {

    /**
     * Begins a transaction. All operations are performed within
     * a single transaction afterwards
     *
     * @throws CustomSqlException if any SQL error occurs
     */
    void begin() throws CustomSqlException;

    /**
     * Commits all executed operations in the transactions. All changes will be
     * saved
     *
     * @throws CustomSqlException if any SQL error occurs
     */
    void commit() throws CustomSqlException;

    /**
     * Rollbacks all executed operations in the transaction. All changes won't be
     * saved. The method is invoked when any exception occurs during performing
     * operations
     *
     * @throws CustomSqlException if any SQL error occurs
     */
    void rollback() throws CustomSqlException;

    /**
     * Closes the current transaction setting the {@code autoCommit} value to {@code true}
     *
     * @throws CustomSqlException if any SQL error occurs
     */
    void close() throws CustomSqlException;

    /**
     * Returns {@code true} if the transaction was begun
     *
     * @return {@code true} if the transaction was begun
     */
    boolean wasBegun();
}
