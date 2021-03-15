package com.nixsolutions.ppp.jdbc.tool;

import java.sql.Connection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class JdbcTransactionTemplate implements TransactionTemplate {

    @Override
    public <T> T executeAndReturn(Supplier<Connection> supplier, Function<Session, T> function) {
        try (Session session = SessionFactory.openSession(supplier.get())) {
            Transaction transaction = session.beginTransaction();
            try {
                T result = function.apply(session); // execute operations
                transaction.commit();
                return result;
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }

    @Override
    public void execute(Supplier<Connection> supplier, Consumer<Session> consumer) {
        try (Session session = SessionFactory.openSession(supplier.get())) {
            Transaction transaction = session.beginTransaction();
            try {
                consumer.accept(session); // execute operations
                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
                throw exception;
            }
        }
    }
}
