package com.nixsolutions.ppp.jdbc.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.Properties;

public class DataSourceFactory {

    private static volatile DataSourceFactory instance;
    private DataSource dataSource;

    private DataSourceFactory() {
    }

    public static DataSourceFactory getInstance() {
        if (instance == null) {
            synchronized (DataSourceFactory.class) {
                if (instance == null) {
                    instance = new DataSourceFactory();
                }
            }
        }
        return instance;
    }

    public void initializeDataSource() {
        Properties properties = readProperties();
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(properties.getProperty("database.driver"));
        dataSource.setUrl(properties.getProperty("database.url"));
        dataSource.setUsername(properties.getProperty("database.username"));
        dataSource.setPassword(properties.getProperty("database.password"));
        this.dataSource = dataSource;
        this.initializeDatabaseSchema();
    }

    private Properties readProperties() {
        ClassLoader loader = getClass().getClassLoader();
        try (InputStream inputStream = loader.getResourceAsStream("database.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeDatabaseSchema() {
        ClassLoader loader = getClass().getClassLoader();
        try (InputStream stream = loader.getResourceAsStream("scripts/schema.sql");
             Reader reader = new InputStreamReader(stream);
             Connection connection = getDataSource().getConnection()) {
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setLogWriter(null); // turn off logging of SQL script
            runner.runScript(reader);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public DataSource getDataSource() {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource;
    }
}
