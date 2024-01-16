package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
public class UserDaoHibernateImpl implements UserDao {
    static final private String url = "jdbc:mysql://localhost:3306/test";
    static final private String user = "root";
    static final private String password = "Lera5434";
    private SessionFactory factory = null;
    static final private String table = "create table if not exists users(id serial primary key, name varchar(50),last_name varchar(100), age int);";
    static final private String drop = "drop table if exists users;";

    public UserDaoHibernateImpl() {
        factory = Util.getConnectionHibernate(url, user, password);
    }

    @Override
    public void createUsersTable() {
        try (Connection connection = Util.getConnection(url, user, password);) {
            PreparedStatement preparedStatement = connection.prepareStatement(table);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection(url, user, password);) {
            PreparedStatement preparedStatement = connection.prepareStatement(drop);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            User user = new User();
            user.setId(1L);
            user.setName(name);
            user.setLastName(lastName);
            user.setAge(age);
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            List<User> list = session.createQuery("from User", User.class).getResultList();
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException();
        }
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException();
        }
    }
}