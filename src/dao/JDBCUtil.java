package dao;

import java.sql.*;

/**
 * 对应不同操作处需要用到的数据库数据提取方法
 */

public class JDBCUtil {

    static Connection connection;
    static Statement stmt;

    public JDBCUtil() {
    }

    /**
     * 初始化
     * 在服务器开始时就连接数据库
     */
    public static void init() throws ClassNotFoundException, SQLException {
        //加载数据库驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //
        String url = "jdbc:mysql://localhost:3306/user";
        String user = "root";
        String password = "123456";
        connection = DriverManager.getConnection(url, user, password);
        stmt = connection.createStatement();

    }
}
