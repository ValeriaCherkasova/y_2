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
    private String url = "jdbc:mysql://localhost:3306/test";
    private String user = "root";
    private String password = "Lera5434";
    private String clean;
    private String table;
    private String drop;
    private String save;
    private String remove;
    private String get;


    public UserDaoJDBCImpl() {

    }

    public Connection setConnection() throws SQLException {
        try {
            return Util.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void createUsersTable() throws SQLException {
        table = "create table if not exists users(id serial primary key, name varchar(50),last_name varchar(100), age int);";
        try {
            Connection connection = setConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(table);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        drop = "drop table if exists users;";
        try {
            Connection connection = setConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(drop);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        save = "insert into users (name, last_name, age) VALUES (?, ?, ?)";
        try {
            Connection connection = setConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(save);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем –" + name +"добавлен в базу данных");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        remove = "delete from users where id = ?;";
        try {
            Connection connection = setConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(remove);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        get = "select * from users;";
        try {
            Connection connection = setConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(get);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList list = new ArrayList<User>();

            while(resultSet.next()){
                User user = new User(resultSet.getString("name"),resultSet.getString("last_name"), resultSet.getByte("age"));
                list.add(user);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        clean = "truncate table users;";
        try {
            Connection connection = setConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(clean);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
