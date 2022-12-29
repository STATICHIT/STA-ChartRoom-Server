package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MessageDao {

    static Statement stmt;

    static {
        try {
            stmt = JDBCUtil.connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //SQL语句
    static final String RETURN_ID = "SELECT pk_ID FROM `user_info` WHERE pk_ID = ? or U_email = ? or U_nickname = ?";
    static final String RETURN_NICK = "SELECT U_nickname FROM `user_info` WHERE pk_ID = ?";
    static final String RETURN_GROUP_NAME = "SELECT G_name FROM `group_info` WHERE G_id = ? ";
    static final String INSERT_MESSAGE = "INSERT INTO message_infos (M_sender,M_getter,M_concent,M_time) VALUES (?,?,?,?)";
    static final String RETURN_AVATAR = "SELECT U_avatar FROM `user_info` WHERE pk_ID = ? or U_nickname = ? ";
    static final String RETURN_GROUP_ID = "SELECT G_id FROM `group_info` WHERE G_id = ? or G_name = ? ";
    static final String INSERT_GROUP_MESSAGE = "INSERT INTO group_message_info (Gr_send_id,Gr_group_id,Gr_concent,Gr_time) VALUES (?,?,?,?)";
    static final String INSERT_COMMON_PHRASES = "INSERT INTO common_phrases_info (C_userId,C_common) VALUES (?,?) ";
    static final String LOAD_COMMON_PHRASES = "SELECT C_common FROM common_phrases_info WHERE C_userId = ? ";


    /**
     * 根据ID或邮箱或昵称返回账号
     *
     * @param data
     * @return
     * @throws SQLException
     */
    //返回id
    public static String returnId(String data) throws SQLException {
        String id = null;
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(RETURN_ID);
        preparedStatement.setObject(1, data);
        preparedStatement.setObject(2, data);
        preparedStatement.setObject(3, data);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            id = resultSet.getString("pk_ID");
        }
        return id;
    }

    /**
     * 根据id返回nick
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public static String returnNick(String id) throws SQLException {
        String nick = null;
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(RETURN_NICK);
        preparedStatement.setObject(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            nick = resultSet.getString("U_nickname");
        }
        return nick;
    }

    /**
     * 根据群id返回群名称
     *
     * @param groupId
     * @return
     * @throws SQLException
     */
    public static String ReturnName(String groupId) throws SQLException {
        String name = null;
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(RETURN_GROUP_NAME);
        preparedStatement.setObject(1, groupId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            name = resultSet.getString("G_name");
        }
//        System.out.println(name);
        return name;
    }

    /**
     * 插入消息记录
     *
     * @param sender
     * @param getter
     * @param content
     * @param time
     * @throws SQLException
     */
    public static void InsertMessage(String sender, String getter, String content, String time) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(INSERT_MESSAGE);
        preparedStatement.setObject(1, sender);
        preparedStatement.setObject(2, getter);
        preparedStatement.setObject(3, content);
        preparedStatement.setObject(4, time);
        preparedStatement.executeUpdate();
    }

    /**
     * 返回头像
     *
     * @param sender
     * @return
     * @throws SQLException
     */
    public static String returnAvatar(String sender) throws SQLException {
        String avatar = null;
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(RETURN_AVATAR);
        preparedStatement.setObject(1, sender);
        preparedStatement.setObject(2, sender);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            avatar = resultSet.getString("U_avatar");
        }
//        System.out.println(avatar);
        return avatar;
    }

    /**
     * 返回群id
     *
     * @param data
     * @return
     * @throws SQLException
     */
    public static String returnGroupId(String data) throws SQLException {
        String id = null;
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(RETURN_GROUP_ID);
        preparedStatement.setObject(1, data);
        preparedStatement.setObject(2, data);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            id = resultSet.getString("G_id");
        }
//        System.out.println(id);
        return id;
    }

    /**
     * 插入群聊信息
     *
     * @param senderId
     * @param groupId
     * @param content
     * @param sendTime
     * @throws SQLException
     */
    public static void InsertGroupMessage(String senderId, String groupId, String content, String sendTime) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(INSERT_GROUP_MESSAGE);
        preparedStatement.setObject(1, senderId);
        preparedStatement.setObject(2, groupId);
        preparedStatement.setObject(3, content);
        preparedStatement.setObject(4, sendTime);
        preparedStatement.executeUpdate();
    }


    /**
     * 添加常用于
     *
     * @param userId
     * @param phrases
     */
    public static void InsertCommonPhrases(String userId, String phrases) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(INSERT_COMMON_PHRASES);
        preparedStatement.setObject(1, userId);
        preparedStatement.setObject(2, phrases);
        preparedStatement.executeUpdate();
    }

    public static ArrayList<String> LoadCommonPhrases(String userId) throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(LOAD_COMMON_PHRASES);
        preparedStatement.setObject(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            list.add(resultSet.getString("C_common"));
        }
        return list;
    }
}
