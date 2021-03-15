package com.nixsolutions.ppp.jdbc.tool;

import java.sql.Connection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The {@code TransactionTemplate} interface is responsible for executing queries
 * that are wrapped in a single transaction. If any exception is thrown during
 * operation execution, then the transaction rollbacks; otherwise, the transaction
 * commits. Each method creates a new {@link Session} using the {@code Connection}
 * delivered with the {@code Supplier<Connection>}
 *
 * @author Serhii Nesterov
 */
public interface TransactionTemplate {

    /**
     * Executes operations located in the {@code function} and returns {@code T} result
     *
     * @param supplier the supplier used to create a {@code Connection}
     * @param function the function containing a set of operations to be executed
     *                 within the transaction
     * @param <T>      the type to be returned
     * @return the object of the {@code T} type
     * @throws RuntimeException if any RuntimeException is thrown
     * @see #execute(Supplier, Consumer)
     */
    <T> T executeAndReturn(Supplier<Connection> supplier,
            Function<Session, T> function) throws RuntimeException;

    /**
     * Executes operations located in the {@code consumer} without returning any result
     *
     * @param supplier the supplier used to create a {@code Connection}
     * @param consumer the consumer containing a set of operations to be executed
     *                 within the transaction
     * @throws RuntimeException if any RuntimeException is thrown
     * @see #executeAndReturn(Supplier, Function)
     */
    void execute(Supplier<Connection> supplier, Consumer<Session> consumer)
            throws RuntimeException;
}
