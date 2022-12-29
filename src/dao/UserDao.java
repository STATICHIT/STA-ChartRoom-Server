package dao;

import chat.*;
import org.apache.commons.mail.Email;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDao {

    static Statement stmt;

    static {
        try {
            stmt = JDBCUtil.connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //SQL语句
    static final String LOGIN = "SELECT pk_ID,U_password FROM `user_info` WHERE (pk_ID = ?  and U_password = ? ) or (U_email = ? and U_password = ? )";
    static final String ENROLL = "INSERT INTO user_info(pk_ID,U_password,U_nickname,U_email,U_sex,U_birthday,U_SignaTure,U_avatar) VALUES (?,?,?,?,'-','暂未设置','此人很懒，没有简介','无')";
    static final String RESET_PASSWORD = "UPDATE user_info SET U_password = ? WHERE U_email = ?";
    static final String FIND_ID = "SELECT pk_ID FROM `user_info` WHERE U_nickname = ?";
    static final String LOAD_USER_DATA = "SELECT pk_ID,U_birthday,U_nickname,U_sex,U_SignaTure,U_avatar FROM `user_info` WHERE pk_ID = ? or U_email = ? or U_nickname = ?";
    static final String EMAIL_ONLY = "SELECT * FROM `user_info` WHERE U_email = ?";
    static final String LOAD_FRIEND = "SELECT user_info.*  FROM `friend_info` LEFT JOIN user_info on  (F_user1 = ? and pk_ID = F_user2) or (F_user2 = ? and pk_ID = F_user1) WhERE pk_ID > 0";
    static final String RESET_NICK = "UPDATE user_info SET U_nickname = ? WHERE pk_ID = ? ";
    static final String RESET_SEX = "UPDATE user_info SET U_sex = ? WHERE pk_ID = ? ";
    static final String RESET_BIRTH = "UPDATE user_info SET U_birthday = ? WHERE pk_ID = ? ";
    static final String RESET_SIGN = "UPDATE user_info SET U_SignaTure = ? WHERE pk_ID = ? ";
    static final String SEARCH_ID = "SELECT pk_ID FROM `user_info` WHERE pk_ID = ?";
    static final String SEARCH_GROUP_ID = "SELECT G_id FROM `group_info` WHERE G_id = ? ";
    static final String ADD_REQUEST = "INSERT INTO addlist_info(A_sender,A_getter,A_sign,A_sendtime) VALUES( ?,?,?,?)";
    static final String LOAD_FRIEND_REQUEST = "SELECT user_info.*,addlist_info.A_sign,addlist_info.A_sendtime FROM addlist_info LEFT JOIN user_info ON (A_getter = ? and pk_ID = A_sender) WHERE pk_ID>0";
    static final String DELETE_FRIEND_REQUEST = "DELETE FROM addlist_info WHERE (A_sender = ? and  A_getter = ? ) or (A_sender = ? and  A_getter = ?)";
    static final String ADD_FRIEND = "INSERT INTO friend_info(F_user1,F_user2) VALUES (?,?)";
    static final String LOAD_MESSAGES = "SELECT user_info.U_nickname,user_info.U_avatar,message_infos.M_time,message_infos.M_concent FROM message_infos LEFT JOIN user_info ON ( M_sender = ? and M_getter = ? and pk_ID = M_sender) OR (M_sender = ? and M_getter = ? and pk_ID = M_sender) WHERE pk_ID>0";
    static final String CHECK_FRIEND = "SELECT pk_nomeanskey FROM `friend_info` WHERE (F_user1 = ? and F_user2 = ? ) or (F_user1 = ? and F_user2 = ?)";
    static final String DELETE_FRIEND = "DELETE FROM friend_info WHERE (F_user1 = ? and  F_user2 = ? ) or (F_user2 = ? and  F_user1 = ? )";
    static final String SET_AVATAR = "UPDATE user_info set U_avatar = ?  where pk_ID = ?";
    static final String CHECK_GROUP = "SELECT Gm_groupid FROM `group_member_info` WHERE Gm_userid = ? and Gm_groupid = ? ";
    static final String CHECK_ONLINE = "SELECT O_userId FROM online_user_info WHERE O_userId = ?";
    static final String RETURN_ID = "SELECT pk_ID FROM user_info WHERE pk_ID = ? or U_email = ? ";
    static final String ADD_ONLINE = "INSERT INTO online_user_info(O_userId) VALUES( ? ) ";
    static final String DELETE_ONLINE = "DELETE FROM online_user_info WHERE O_userId = ? ";
    static final String CHECK_NICK = "SELECT U_nickname FROM `user_info` WHERE U_nickname = ? ";


    /**
     * 登录所需提取的数据：
     * 账号，密码
     * 传参传入用户输入的账号密码，在数据库中查找是否存在
     * 用于比对是否匹配，用户是否合法-
     *
     * @param id
     * @param ps
     * @return
     * @throws SQLException
     */
    public static boolean loginHandler(String id, String ps) throws SQLException {
        boolean b = false;
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(LOGIN);
        preparedStatement.setObject(1, id);
        preparedStatement.setObject(2, ps);
        preparedStatement.setObject(3, id);
        preparedStatement.setObject(4, ps);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            b = true;
        }
        return b;
    }


    /**
     * 注册
     * 将客户端传来的昵称，密码，账号，邮箱号填入表中
     *
     * @param id
     * @param ps
     * @param nick
     * @param email
     * @throws SQLException
     */
    public static void insertUser(String id, String ps, String nick, String email) throws SQLException {
        //因为数据库用户表中用户ID的类型是整型，所以要把String转化为int
        int ID = Integer.parseInt(id);
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(ENROLL);
        preparedStatement.setObject(1, ID);
        preparedStatement.setObject(2, ps);
        preparedStatement.setObject(3, nick);
        preparedStatement.setObject(4, email);
        preparedStatement.executeUpdate();
    }

    /**
     * 修改密码
     * 用新密码替换旧密码
     *
     * @param ps
     * @param email
     * @throws SQLException
     */
    public static void resetPassword(String ps, String email) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(RESET_PASSWORD);
        preparedStatement.setObject(1, ps);
        preparedStatement.setObject(2, email);
        preparedStatement.executeUpdate();
    }

    /**
     * 根据昵称返回ID
     *
     * @param nickname
     * @return
     * @throws SQLException
     */
    public static String findId(String nickname) throws SQLException {
        String Id = null;
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(FIND_ID);
        preparedStatement.setObject(1, nickname);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Id = resultSet.getString("pk_ID");
        }
//        System.out.println(Id);
        return Id;
    }

    /**
     * 在数据库中查询该用户资料
     *
     * @param data
     * @throws SQLException
     */
    public static User findUserInfo(String data) throws SQLException {

        User user = new User();
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(LOAD_USER_DATA);
        preparedStatement.setObject(1, data);
        preparedStatement.setObject(2, data);
        preparedStatement.setObject(3, data);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            user.setBirth(resultSet.getString("U_birthday"));
            user.setNickName(resultSet.getString("U_nickname"));
            user.setSex(resultSet.getString("U_sex"));
            user.setSign(resultSet.getString("U_SignaTure"));
            user.setUserId(resultSet.getString("pk_ID"));
            user.setAvatar(resultSet.getString("U_avatar"));
        }
        return user;
    }

    /**
     * 邮箱查重
     * 在数据库中查询该邮箱是否已被绑定
     * 已被绑定返回true
     * 未被绑定返回false
     *
     * @param Email
     * @return
     * @throws SQLException
     */
    //邮箱查重
    public static boolean emailOnly(String Email) throws SQLException {
        boolean b = false;
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(EMAIL_ONLY);
        preparedStatement.setObject(1, Email);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            b = true;
        }
        return b;
    }

    /**
     * 根据用户id获取好友列表
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    public static ArrayList findFriend(String userId) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(LOAD_FRIEND);
        preparedStatement.setObject(1, userId);
        preparedStatement.setObject(2, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<ViewList> dataList = new ArrayList<>();
        while (resultSet.next()) {
            dataList.add(new ViewList(resultSet.getString("U_nickname"), resultSet.getString("U_SignaTure"), resultSet.getString("U_avatar")));
        }
//        for (ViewList a : dataList) {
//            System.out.println(a);
//        }
        return dataList;
    }

    /**
     * 修改个人资料
     *
     * @param user
     * @throws SQLException
     */
    public static void resetPrivateData(User user) throws SQLException {
        String Id = user.getUserId();
        String nickName = user.getNickName();
        String sex = user.getSex();
        String birth = user.getBirth();
        String sign = user.getSign();
        //未填写则不修改
        if (nickName != null) {
            PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(RESET_NICK);
            preparedStatement.setObject(1, nickName);
            preparedStatement.setObject(2, Id);
            preparedStatement.executeUpdate();
        }
        if (sex != null) {
            PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(RESET_SEX);
            preparedStatement.setObject(1, sex);
            preparedStatement.setObject(2, Id);
            preparedStatement.executeUpdate();
        }
        if (birth != null) {
            PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(RESET_BIRTH);
            preparedStatement.setObject(1, birth);
            preparedStatement.setObject(2, Id);
            preparedStatement.executeUpdate();
        }
        if (sign != null) {
            PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(RESET_SIGN);
            preparedStatement.setObject(1, sign);
            preparedStatement.setObject(2, Id);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * 查询用户或群组是否存在
     *
     * @param Id
     * @throws SQLException
     */
    public static int searchId(String Id) throws SQLException {
        int b = 0;
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(SEARCH_ID);
        preparedStatement.setObject(1, Id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            b = 1;
        }
        PreparedStatement preparedStatement2 = JDBCUtil.connection.prepareStatement(SEARCH_GROUP_ID);
        preparedStatement2.setObject(1, Id);
        ResultSet resultSet2 = preparedStatement2.executeQuery();
        while (resultSet2.next()) {
            b = 2;
        }
        return b;
    }

    /**
     * 客户发起添加申请，将该申请加到申请表中
     *
     * @param message
     * @throws SQLException
     */
    public static void addNewRequest(Message message) throws SQLException {
        String sendId = message.getSender();
        String getId = message.getGetter();
        String sign = message.getContent();
        String time = message.getSendTime();
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(ADD_REQUEST);
        preparedStatement.setObject(1, sendId);
        preparedStatement.setObject(2, getId);
        preparedStatement.setObject(3, sign);
        preparedStatement.setObject(4, time);
        preparedStatement.executeUpdate();
    }

    /**
     * 加载用户的好友申请表
     *
     * @param id
     * @return
     */
    public static ArrayList<AddList> findAddList(String id) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(LOAD_FRIEND_REQUEST);
        preparedStatement.setObject(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<AddList> addList = new ArrayList<AddList>();
        while (resultSet.next()) {
            addList.add(new AddList(resultSet.getString("U_nickname"), resultSet.getString("pk_ID"), resultSet.getString("U_avatar"),
                    resultSet.getString("U_sex"), resultSet.getString("U_birthday"), resultSet.getString("A_sign"), resultSet.getString("A_sendtime")));
        }
//        for (AddList a : addList) {
//            System.out.println(a);
//        }
        return addList;
    }

    /**
     * 删除好友申请
     *
     * @param sender
     * @param getter
     * @throws SQLException
     */
    public static void DeleteAddList(String sender, String getter) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(DELETE_FRIEND_REQUEST);
        preparedStatement.setObject(1, sender);
        preparedStatement.setObject(2, getter);
        preparedStatement.setObject(3, getter);
        preparedStatement.setObject(4, sender);
//        System.out.println(preparedStatement);

        preparedStatement.executeUpdate();
    }

    /**
     * 添加好友关系
     *
     * @param sender
     * @param getter
     * @throws SQLException
     */
    public static void AddFriend(String sender, String getter) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(ADD_FRIEND);
        preparedStatement.setObject(1, sender);
        preparedStatement.setObject(2, getter);
        preparedStatement.executeUpdate();
    }

    /**
     * 加载聊天消息列表
     *
     * @param user1
     * @param user2
     * @return
     * @throws SQLException
     */
    public static ArrayList<MessageList> findMessageList(String user1, String user2) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(LOAD_MESSAGES);
        preparedStatement.setObject(1, user1);
        preparedStatement.setObject(2, user2);
        preparedStatement.setObject(3, user2);
        preparedStatement.setObject(4, user1);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<MessageList> messageList = new ArrayList<>();

        while (resultSet.next()) {
            messageList.add(new MessageList(resultSet.getString("U_nickname"), resultSet.getString("M_concent"), resultSet.getString("M_time"), resultSet.getString("U_avatar")));
        }
//
//        for (MessageList a : messageList) {
//            System.out.println(a);
//        }
        return messageList;
    }

    /**
     * 确认朋友关系
     *
     * @param user1
     * @param user2
     * @return
     * @throws SQLException
     */
    public static boolean checkFriend(Object user1, String user2) throws SQLException {
        boolean b = false;
//        String sql ="SELECT pk_nomeanskey FROM `friend_info`\n" +
//                "WHERE (F_user1 = '"+ user1 +"' and F_user2 = '"+ user2 +"') or (F_user1 = '"+ user2 +"' and F_user2 = '"+ user1 +"');";
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(CHECK_FRIEND);
        preparedStatement.setObject(1, user1);
        preparedStatement.setObject(2, user2);
        preparedStatement.setObject(3, user2);
        preparedStatement.setObject(4, user1);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            b = true;
        }
        return b;
    }

    /**
     * 删除好友
     *
     * @param user1
     * @param user2
     * @throws SQLException
     */
    public static void DeleteFriend(String user1, String user2) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(DELETE_FRIEND);
        preparedStatement.setObject(1, user1);
        preparedStatement.setObject(2, user2);
        preparedStatement.setObject(3, user1);
        preparedStatement.setObject(4, user2);
        preparedStatement.executeUpdate();
    }

    /**
     * 设置头像
     *
     * @param id
     * @param url
     */
    public static void SetAvatar(String id, String url) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(SET_AVATAR);
        preparedStatement.setObject(1, url);
        preparedStatement.setObject(2, id);
        preparedStatement.executeUpdate();
    }

    /**
     * 查找此人是否已加此群
     *
     * @param objectId
     * @param userId
     * @return
     */
    public static boolean checkGroup(String objectId, String userId) throws SQLException {
        boolean b = false;
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(CHECK_GROUP);
        preparedStatement.setObject(1, userId);
        preparedStatement.setObject(2, objectId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            b = true;
        }
        return b;
    }

    /**
     * 根据邮箱或id返回用户id
     * @param data
     * @return
     */
    public static String ReturnId(String data) throws SQLException {
        String userId = null;
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(RETURN_ID);
        preparedStatement.setObject(1, data);
        preparedStatement.setObject(2, data);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            userId = resultSet.getString("pk_ID");
        }
        return userId;
    }

    /**
     * 查找用户是否在线
     * @param userId
     */
    public static Boolean CheckOnline(String userId) throws SQLException {
        boolean b = false;
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(CHECK_ONLINE);
        preparedStatement.setObject(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            b = true;
        }
        return b;
    }

    /**
     * 增加在线用户
     * @param userId
     */
    public static void AddOnlineUser(String userId) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(ADD_ONLINE);
        preparedStatement.setObject(1, userId);
        preparedStatement.executeUpdate();
    }

    /**
     * 删除在线用户
     * @param userId
     */
    public static void DeleteOnlineUser(String userId) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(DELETE_ONLINE);
        preparedStatement.setObject(1, userId);
        preparedStatement.executeUpdate();
    }

    /**
     * 昵称查重
     * @param name
     * @return
     */
    public static Boolean CheckName(String name) throws SQLException {
        boolean b = false;
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(CHECK_NICK);
        preparedStatement.setObject(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            b = true;
        }
        return b;
    }
}
