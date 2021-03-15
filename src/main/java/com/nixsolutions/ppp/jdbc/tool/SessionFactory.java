package com.nixsolutions.ppp.jdbc.tool;

import java.sql.Connection;

public class SessionFactory {

    public static Session openSession(Connection connection) {
        return new SimpleJdbcSession(connection, new SimpleJdbcTransaction(connection));
    }
}
