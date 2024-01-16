package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    public static Connection getConnection(String url, String user, String password) {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public static SessionFactory getConnectionHibernate(String url, String user, String password) {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", url);
        configuration.setProperty("hibernate.connection.username", user);
        configuration.setProperty("hibernate.connection.password", password);
        configuration.addAnnotatedClass(User.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }
}