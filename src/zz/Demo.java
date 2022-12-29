package zz;

import chat.Message;
import chat.User;
import dao.JDBCUtil;
import dao.MessageDao;

import java.nio.file.attribute.UserPrincipal;
import java.sql.SQLException;

import static dao.JDBCUtil.init;

public class Demo {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        JDBCUtil jdbc = new JDBCUtil();
        jdbc.init();

        //jdbc.insertUser();
        //jdbc.loginHandler();
        //jdbc.insertUser("1","66","你","2394412110@qq.com");
//        UserDao.findUserInfo("100001");
//        UserDao.findFriend(100001);
//        System.out.println(MessageDao.returnId("ren"));
//        UserDao.findFriend("100002");

//        User user = new User();
//        user.setUserId("41014528");user.setSex("女");user.setBirth(null);user.setNickName("宝贝");user.setSign(null);
//        UserDao.resetPrivateData(user);

//        System.out.println(UserDao.searchId("100001"));

//        Message message = new Message();
//        message.setSender("1");
//        message.setGetter("2");
//        message.setContent("你好");
//        UserDao.addNewRequest(message);

//        UserDao.findAddList("100002");
//        UserDao.AddFriend("1","2");
//        UserDao.findAddList("100001");

//        UserDao.findMessageList("100001","100003");
//        UserDao.insertUser("123","1","静砸","23@qq.com");

//        UserDao.DeleteFriend("100001","100005");
//        MessageDao.InsertMessage(new Message("100001","100002","怎么不回我消息","22:22"));
//   MessageDao.returnNick("100001");
//        UserDao.findMessageList("100001","100002");
//        UserDao.findGroupList("100001");
//        GroupDao.AddNewMember("16","100008","1");
//        GroupDao.LoadGroupInfo("摆烂大队");
//    MessageDao.ReturnName("11")
//    ;
//        Message message = new Message();
//        message.setSender("1");
//        message.setGetter("2");
//        message.setContent("你好");
//        message.setSendTime("1");
//        GroupDao.addNewRequest(message);
//        GroupDao.DeleteGroupAddList("100004", "11");

        MessageDao.LoadCommonPhrases("100001");
    }


}
