package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    static private final String CLEAN = "truncate table users;";
    static private final String TABLE = "create table if not exists users(id serial primary key, name varchar(50),last_name varchar(100), age int);";
    static private final String DROP = "drop table if exists users;";
    static private final String SAVE = "insert into users (name, last_name, age) VALUES (?, ?, ?)";
    static private final String REMOVE = "delete from users where id = ?;";
    static private final String GET = "select * from users;";

    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Connection connection = Util.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(TABLE);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Исключение:" + e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(DROP);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Исключение:" + e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем –" + name + "добавлен в базу данных");

        } catch (SQLException e) {
            System.out.println("Исключение:" + e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Исключение:" + e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Connection connection = Util.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<User> list = new ArrayList<User>();

            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"), resultSet.getString("last_name"), resultSet.getByte("age"));
                list.add(user);
            }
            return list;
        } catch (SQLException e) {
            System.out.println("Исключение:" + e);
            return null;
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();) {
            PreparedStatement preparedStatement = connection.prepareStatement(CLEAN);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Исключение:" + e);
        }
    }
}
