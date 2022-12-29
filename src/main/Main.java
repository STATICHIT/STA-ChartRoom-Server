package main;

import dao.JDBCUtil;
import service.Server;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        //连接数据库
        try {
            JDBCUtil.init();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
