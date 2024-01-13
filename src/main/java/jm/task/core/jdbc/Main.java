package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Tomi", "Holi", (byte) 20);
        userService.saveUser("Tomu", "Holu", (byte) 24);
        userService.saveUser("Tomo", "Holo", (byte) 28);
        userService.saveUser("Tome", "Hole", (byte) 30);
        System.out.println(userService.getAllUsers());
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}