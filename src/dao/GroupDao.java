package dao;

import chat.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GroupDao {

    static Statement stmt;

    static {
        try {
            stmt = JDBCUtil.connection.createStatement();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    //SQL语句
    static final String LOAD_GROUP_LIST = "SELECT group_info.* FROM group_member_info LEFT JOIN group_info ON ( Gm_userid = ? and G_id = Gm_groupid) WHERE G_id>0";
    static final String LOAD_GROUP_INFO = "SELECT * FROM `group_info` WHERE G_name = ? ";
    static final String LOAD_GROUP_MEMBER = "SELECT user_info.U_avatar,U_nickname FROM `group_member_info` LEFT JOIN `user_info` ON (Gm_groupid = ? and Gm_status = ? and pk_ID = Gm_userid ) WHERE pk_ID>0";
    static final String LOAD_GRADE = "SELECT group_member_info.Gm_status FROM `group_member_info` WHERE Gm_groupid = ? and Gm_userid = ? ";
    static final String CREAT_GROUP = "INSERT INTO group_info(G_id,G_name,G_num,G_avatar,G_own,G_sign) VALUES (?,?,?,'无',?,'群主很懒,什么都没有留下')";
    static final String ADD_GROUP_NUMBER = "INSERT INTO group_member_info(Gm_groupid,Gm_userid,Gm_status) VALUES (?,?,?)";
    static final String SET_AVATAR = "UPDATE group_info set G_avatar = ?  where G_id = ?";
    static final String FIND_MEMBER = "SELECT Gm_userid FROM group_member_info WHERE Gm_groupid = ? and Gm_userid != ? ";
    static final String LOAD_GROUP_MESSAGES = "SELECT user_info.U_nickname,user_info.U_avatar,group_message_info.Gr_time,group_message_info.Gr_concent \n" +
            "FROM group_message_info LEFT JOIN user_info ON (Gr_group_id = ? and pk_ID = Gr_send_id) WHERE pk_ID>0";
    static final String DELETE_MEMBER = "DELETE FROM group_member_info WHERE Gm_groupid = ? and Gm_userid = ? ";
    static final String UPDATA_GRADE = "UPDATE group_member_info SET  Gm_status= ? WHERE Gm_groupid = ? and Gm_userid = ?  ";
    static final String RESET_GROUP_SIGN = "UPDATE group_info SET  G_sign = ? WHERE G_id = ?;";
    static final String RESET_GROUP_NAME = "UPDATE group_info SET  G_name = ? WHERE G_id = ?;";
    static final String DELETE_GROUP = "DELETE FROM group_info WHERE G_id = ? ";
    static final String DELETE_GROUP_Member = "DELETE FROM group_member_info WHERE Gm_groupid = ? ";
    static final String DELETE_GROUP_Message = "DELETE FROM group_message_info WHERE Gr_group_id = ?  ";
    static final String ADD_REQUEST = "INSERT INTO add_group_list_info(Ag_sender,Ag_groupId,Ag_sign,Ag_sendtime) VALUES(?,?,?,?)";
    static final String TARGET_GROUP = "SELECT Gm_groupid FROM group_member_info WHERE Gm_userid = ? and(Gm_status = '1' or Gm_status = '2')";
    static final String LOAD_ADD_LIST = "SELECT user_info.*,add_group_list_info.Ag_sign,add_group_list_info.Ag_sendtime " +
            "FROM add_group_list_info LEFT JOIN user_info ON (Ag_groupId = ? and Ag_Sender = pk_ID) WHERE pk_ID>0";
    static final String DELETE_ADD_REQUEST = "DELETE FROM add_group_list_info WHERE Ag_sender = ? and Ag_groupId = ? ";
    static final String UPDATE_GROUP_OWN = "UPDATE  group_info set G_own = ? WHERE G_id = ?";

    /**
     * 加载群组列表
     * @param id
     * @return
     * @throws SQLException
     */
    public static ArrayList<ViewList> findGroupList(String id) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(LOAD_GROUP_LIST);
        preparedStatement.setObject(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<ViewList> groupList = new ArrayList<>();

        while (resultSet.next()) {
            groupList.add(new ViewList(resultSet.getString("G_name"), resultSet.getString("G_id"), resultSet.getString("G_avatar")));
        }
//        for (ViewList a : groupList) {
//            System.out.println(a);
//        }
        return groupList;
    }


    /**
     * 加载群资料
     * @param groupName
     * @param userId
     * @return
     * @throws SQLException
     */
    public static GroupInfo LoadGroupInfo(String groupName, String userId) throws SQLException {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setName(groupName);
        //群基本信息
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(LOAD_GROUP_INFO);
        preparedStatement.setObject(1, groupName);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            groupInfo.setAvatar(resultSet.getString("G_avatar"));
            groupInfo.setId(resultSet.getString("G_id"));
            groupInfo.setSign(resultSet.getString("G_sign"));
            groupInfo.setNum(resultSet.getString("G_num"));
        }
        //群成员信息
        //查找群主信息
        PreparedStatement preparedStatement2 = JDBCUtil.connection.prepareStatement(LOAD_GROUP_MEMBER);
        preparedStatement2.setObject(1, groupInfo.getId());
        preparedStatement2.setObject(2, "1");
        ResultSet resultSet2 = preparedStatement2.executeQuery();
        while (resultSet2.next()) {
            groupInfo.setOwnNick(resultSet2.getString("U_nickname"));
            groupInfo.setOwnImage(resultSet2.getString("U_avatar"));
        }

        //查找群管理信息
        PreparedStatement preparedStatement3 = JDBCUtil.connection.prepareStatement(LOAD_GROUP_MEMBER);
        preparedStatement3.setObject(1, groupInfo.getId());
        preparedStatement3.setObject(2, "2");
        ResultSet resultSet3 = preparedStatement3.executeQuery();
        ArrayList<ViewList> manageList = new ArrayList<>();
        while (resultSet3.next()) {
            ViewList data = new ViewList();
            data.setId(resultSet3.getString("U_nickname"));
            data.setImage(resultSet3.getString("U_avatar"));
            manageList.add(data);
        }
        groupInfo.setManageList(manageList);

        //查找普通成员信息
        PreparedStatement preparedStatement4 = JDBCUtil.connection.prepareStatement(LOAD_GROUP_MEMBER);
        preparedStatement4.setObject(1, groupInfo.getId());
        preparedStatement4.setObject(2, "3");
        ResultSet resultSet4 = preparedStatement4.executeQuery();
        ArrayList<ViewList> otherList = new ArrayList<>();
        while (resultSet4.next()) {
            ViewList data = new ViewList();
            data.setId(resultSet4.getString("U_nickname"));
            data.setImage(resultSet4.getString("U_avatar"));
            otherList.add(data);
        }
        groupInfo.setOtherList(otherList);
        //查找本人在群里的身份
        groupInfo.setContent(GroupDao.CheckGrade(MessageDao.returnGroupId(groupName), userId));
//        System.out.println("这里这里这里这里！！"+groupInfo);//输出测试
        return groupInfo;
    }

    /**
     * 查找群组最基本的信息
     * @param groupId
     * @return
     * @throws SQLException
     */
    public static GroupInfo LoadGroupBasicInfo(String groupId) throws SQLException {
        String groupName = MessageDao.ReturnName(groupId);
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setName(groupName);
        //群基本信息
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(LOAD_GROUP_INFO);
        preparedStatement.setObject(1, groupName);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            groupInfo.setAvatar(resultSet.getString("G_avatar"));
            groupInfo.setId(resultSet.getString("G_id"));
            groupInfo.setSign(resultSet.getString("G_sign"));
            groupInfo.setNum(resultSet.getString("G_num"));
        }
        return groupInfo;
    }


    /**
     * 查找用户在群里的身份
     * @param GroupId
     * @param userId
     * @return
     * @throws SQLException
     */
    public static String CheckGrade(String GroupId, String userId) throws SQLException {
        String Grade = new String();
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(LOAD_GRADE);
        preparedStatement.setObject(1, GroupId);
        preparedStatement.setObject(2, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Grade = resultSet.getString("Gm_status");
        }
//        System.out.println(Grade);
        return Grade;
    }

    /**
     * 增加新群员
     * @param groupId
     * @param userId
     * @param status
     * @throws SQLException
     */
    public static void AddNewMember(String groupId, String userId, String status) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(ADD_GROUP_NUMBER);
        preparedStatement.setObject(1, groupId);
        preparedStatement.setObject(2, userId);
        preparedStatement.setObject(3, status);
        preparedStatement.executeUpdate();
    }

    /**
     * 创建群
     * @param ownId
     * @param groupId
     * @param groupName
     * @param numberNum
     * @throws SQLException
     */
    public static void CreatGroup(String ownId, String groupId, String groupName, int numberNum) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(CREAT_GROUP);
        preparedStatement.setObject(1, groupId);
        preparedStatement.setObject(2, groupName);
        preparedStatement.setObject(3, numberNum);
        preparedStatement.setObject(4, ownId);
        preparedStatement.executeUpdate();
    }

    /**
     * 设置群头像
     * @param id
     * @param url
     * @throws SQLException
     */
    public static void SetGroupAvatar(String id, String url) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(SET_AVATAR);
        preparedStatement.setObject(1, url);
        preparedStatement.setObject(2, id);
        preparedStatement.executeUpdate();
    }

    /**
     * 查找某群组除了查询者的全体成员
     * @param groupId
     * @param senderId
     * @return
     * @throws SQLException
     */
    public static ArrayList<String> GetGroupMemberBesidesSelf(String groupId, String senderId) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(FIND_MEMBER);
        preparedStatement.setObject(1, groupId);
        preparedStatement.setObject(2, senderId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<String> member = new ArrayList<>();
        while (resultSet.next()) {
            member.add(resultSet.getString("Gm_userid"));
        }
//        System.out.println(member);
        return member;
    }

    /**
     * 加载群组消息
     * @param groupID
     * @return
     * @throws SQLException
     */
    public static ArrayList<MessageList> LoadMessageList(String groupID) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(LOAD_GROUP_MESSAGES);
        preparedStatement.setObject(1, groupID);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<MessageList> messageList = new ArrayList<>();

        while (resultSet.next()) {
            messageList.add(new MessageList(resultSet.getString("U_nickname"), resultSet.getString("Gr_concent"), resultSet.getString("Gr_time"), resultSet.getString("U_avatar")));
        }
//        for (MessageList a : messageList) {
//            System.out.println(a);
//        }
        return messageList;
    }

    /**
     * 删除群成员
     * @param groupId
     * @param userId
     * @throws SQLException
     */
    public static void DeleteMember(String groupId, String userId) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(DELETE_MEMBER);
        preparedStatement.setObject(1, groupId);
        preparedStatement.setObject(2, userId);
        preparedStatement.executeUpdate();
    }

    /**
     * 改变群员等级
     * @param groupId
     * @param userId
     * @param grade
     * @throws SQLException
     */
    public static void UpdateGrade(String groupId, String userId, String grade) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(UPDATA_GRADE);
        preparedStatement.setObject(1, grade);
        preparedStatement.setObject(2, groupId);
        preparedStatement.setObject(3, userId);
        preparedStatement.executeUpdate();
    }

    /**
     * 更新群组信息
     * @param name
     * @param sign
     * @param groupId
     * @throws SQLException
     */
    public static void UpdateGroupData(String name, String sign, String groupId) throws SQLException {

        if (sign != null) {
            PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(RESET_GROUP_SIGN);
            preparedStatement.setObject(1, sign);
            preparedStatement.setObject(2, groupId);
            preparedStatement.executeUpdate();
        }

        if (name != null) {
            PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(RESET_GROUP_NAME);
            preparedStatement.setObject(1, name);
            preparedStatement.setObject(2, groupId);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * 删除群成员
     * @param groupId
     * @throws SQLException
     */
    public static void DeleteMember(String groupId) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(DELETE_GROUP_Member);
        preparedStatement.setObject(1, groupId);
        preparedStatement.executeUpdate();
    }

    /**
     * 删除群消息记录
     * @param groupId
     * @throws SQLException
     */
    public static void DeleteMessages(String groupId) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(DELETE_GROUP_Message);
        preparedStatement.setObject(1, groupId);
        preparedStatement.executeUpdate();
    }

    /**
     * 删除群组
     * @param groupId
     * @throws SQLException
     */
    public static void DeleteGroup(String groupId) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(DELETE_GROUP);
        preparedStatement.setObject(1, groupId);
        preparedStatement.executeUpdate();
    }

    /**
     * 加群申请
     * @param message
     * @throws SQLException
     */
    public static void addNewRequest(Message message) throws SQLException {
        String sendId = message.getSender();
        String groupId = message.getGetter();
        String sign = message.getContent();
        String time = message.getSendTime();
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(ADD_REQUEST);
        preparedStatement.setObject(1, sendId);
        preparedStatement.setObject(2, groupId);
        preparedStatement.setObject(3, sign);
        preparedStatement.setObject(4, time);
        preparedStatement.executeUpdate();
    }

    /**
     * 返回群组申请列表
     * @param userId
     * @return
     * @throws SQLException
     */
    public static ArrayList<AddList> FindAddList(String userId) throws SQLException {
        ArrayList<AddList> addGroupList = new ArrayList<>();
        //先查找本人身份为1或2的群聊Id
        ArrayList<String> groupList = GroupDao.FindTargetGroup(userId);
        //再查找这些群的申请
        for (String groupId : groupList) {
            PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(LOAD_ADD_LIST);
            preparedStatement.setObject(1, groupId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                addGroupList.add(new AddList(resultSet.getString("U_nickname"), resultSet.getString("pk_ID"), resultSet.getString("U_avatar"), resultSet.getString("U_sex"),
                        resultSet.getString("U_birthday"), resultSet.getString("Ag_sign"), resultSet.getString("Ag_sendtime"), MessageDao.ReturnName(groupId)));
            }
        }
//        for(AddList a:addGroupList){
//            System.out.println(a);
//        }
        return addGroupList;
    }

    /**
     * 返回用户本人身份为1或2的群聊Id
     * @param userId
     * @return
     * @throws SQLException
     */
    public static ArrayList<String> FindTargetGroup(String userId) throws SQLException {
        ArrayList<String> groupList = new ArrayList<>();
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(TARGET_GROUP);
        preparedStatement.setObject(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            groupList.add(resultSet.getString("Gm_groupid"));
        }
        return groupList;
    }

    /**
     * 删除加群申请
     * @param userId
     * @param groupId
     * @throws SQLException
     */
    public static void DeleteGroupAddList(String userId, String groupId) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(DELETE_ADD_REQUEST);
        preparedStatement.setObject(1, userId);
        preparedStatement.setObject(2, groupId);
        preparedStatement.executeUpdate();
    }

    /**
     * 更改群组的群主
     * @param groupId
     * @param newOwnId
     * @throws SQLException
     */
    public static void UpdateGroupOwn(String groupId, String newOwnId) throws SQLException {
        PreparedStatement preparedStatement = JDBCUtil.connection.prepareStatement(UPDATE_GROUP_OWN);
        preparedStatement.setObject(1, groupId);
        preparedStatement.setObject(2, newOwnId);
        preparedStatement.executeUpdate();
    }
}
