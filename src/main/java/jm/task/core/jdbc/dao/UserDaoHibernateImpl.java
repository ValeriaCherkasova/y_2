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
    private String url = "jdbc:mysql://localhost:3306/test";
    private String user = "root";
    private String password = "Lera5434";
    private Session session;
    private String table = "create table if not exists users(id serial primary key, name varchar(50),last_name varchar(100), age int);";
    private String drop = "drop table if exists users;";

    public UserDaoHibernateImpl() {
        SessionFactory factory = Util.getConnectionHibernate(url, user, password);
        session = factory.openSession();
    }

    private Connection setConnection() throws SQLException {
        try {
            return Util.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createUsersTable() {
        try {
            Connection connection = setConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(table);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            Connection connection = setConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(drop);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = session.beginTransaction();
        try {
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
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = session.beginTransaction();
        try {
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = session.beginTransaction();
        try {
            List<User> list = session.createQuery("from User", User.class).getResultList();
            transaction.commit();
            return list;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = session.beginTransaction();
        try {
            session.createQuery("delete from User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
