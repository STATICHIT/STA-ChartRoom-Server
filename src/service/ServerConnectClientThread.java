package service;

import chat.*;
import dao.GroupDao;
import dao.MessageDao;
import dao.UserDao;
import util.ImageSaveUtil.ImageSave;
import util.encryption.MD5Util;
import util.sendemail.VerifiCode;
import util.snowID.SnowFlake;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import static util.sendemail.SendEmailUtil.sendEmail;

/**
 * 该类的一个对象和某个客户端保持通信
 */
public class ServerConnectClientThread extends Thread {

    private Socket socket;//该线程持有的socket
    private Boolean ThreadState = true;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;

    public ServerConnectClientThread(Socket socket) {
        this.socket = socket;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    @Override
    public void run() {//发送or接受客户端消息
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (ThreadState) {
            try {
                System.out.println("服务端和客户端" + socket + "保持通信，读取数据...");
                //请求处理之后的响应

                //如果客户端没有发送Request对象，线程会阻塞在这里
                Request request = (Request) ois.readObject();
                Response response = new Response();//要发回去的反馈

                switch (request.getMesType()) {
                    //当接收到的是登录请求
                    case "1": {
                        User user = (User) request.getData();
                        //服务端传到数据库匹配 二次加密
                        user.setPasswd(MD5Util.MD5Encode(user.getPasswd(), "utf8"));
                        //验证
                        if (UserDao.loginHandler(user.getUserId(), user.getPasswd())) {
                            //在控制台上也输出一个提示
                            System.out.println("<==欢迎(用户" + user.getUserId() + ")登录==>");
                            //加载个人资料传回去
                            response.setData(UserDao.findUserInfo(user.getUserId()));
                            response.setResType(ResponseType.RESPONSE_LOGIN_SUCCEED);
                            //将Response对象回复给客户端
                            oos.writeObject(response);
                            oos.flush();

                            //这块注释的地方是本来想 把线程名改为持有的用户id 然后再将线程存入集合
                            //想根据线程名调用线程 再获取用户传输socket 但是发现难以根据线程名调用线程
                            //所以改变方法： 用hashmap存socket而不是线程
//                        currentThread().setName(user.getUserId());
//                        System.out.println("用户"+user.getUserId()+"连接的服务器线程名字未"+currentThread().getName());
////                        //把该线程对象放入ConcurrentHashMap集合中，便于管理
////                        ManageClientThreads.addClientThread(user.getUserId(),(ServerConnectClientThread) currentThread());
//                        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
//                        Thread thrds[] = new Thread[threadGroup.activeCount()];
//                        threadGroup.enumerate(thrds);
//                        for (Thread t : thrds){
//                            System.out.println(t.getName());
//                        }
                            //存socket之后出现了同一个socket多次new流的问题，所以还是存线程
                            //存储流程变成，登录成功之后把用户id和当前线程（this指代）存入集合
                            ManageClientThreads.addClientThread(user.getUserId(), this);

                        } else {
                            //在控制台上也输出一个提示
                            System.out.println("账号或密码输入错误");
                            response.setResType(ResponseType.RESPONSE_LOGIN_FAIL);
                            //将Response对象回复给客户端
                            oos.writeObject(response);
                            oos.flush();
                        }
                    }
                    break;
                    //当接收到的是注册请求
                    case "2": {
                        User user = (User) request.getData();
                        //验证
                        System.out.println("用户填的验证码是" + user.getVcode());
                        if (user.getVcode().equals(ManageEmailVcode.getEmailVcode(user.getemail()))) {
                            //说明验证码匹配正确，注册成功
                            user.setVcode(null);
                            ManageEmailVcode.removeEmailVcode(user.getemail());
                            response.setResType(ResponseType.RESPONSE_ENROLL_SUCCEED);

                            //雪花算法生成一个id，把"注册成功的消息"和“生成的id”传回客户端
                            SnowFlake idWoker = new SnowFlake(1L, 1L);
                            String id = String.valueOf(idWoker.nextId());

                            response.setContent(id);
                            //将response对象回复给客户端
                            oos.writeObject(response);
                            oos.flush();

                            //服务端传到数据库 二次加密
                            user.setPasswd(MD5Util.MD5Encode(user.getPasswd(), "utf8"));

                            //把该id以及客户端传来的消息存到数据库中的用户表中
                            UserDao.insertUser(id, user.getPasswd(), user.getNickName(), user.getemail());

                        } else {
                            response.setResType(ResponseType.RESPONSE_ENROLL_FAIL);
                            //将response对象回复给客户端
                            oos.writeObject(response);
                            oos.flush();
                        }
                    }
                    break;
                    //当接收到的是发送验证码邮件请求
                    case "3": {
                        VerifiCode s = new VerifiCode();
                        String vcode = s.getC();
                        User user = (User) request.getData();
                        ManageEmailVcode.AddEmailVcode(user.getemail(), vcode);
                        System.out.println("邮箱" + user.getemail() + "对应的验证码是" + ManageEmailVcode.getEmailVcode(user.getemail()));
                        //sendEmail(user.getemail(), vcode);
                        System.out.println("你的验证码是"+vcode);
                        System.out.println("发送成功！");
                    }
                    break;
                    //请求返回好友列表
                    case "4": {
                        String id = (String) request.getData();
                        ArrayList<ViewList> friendList = UserDao.findFriend(id);
                        if (friendList == null) {
                            response.setResType(ResponseType.RESPONSE_RETURN_FRIENDS_FAIL);
                        } else {
                            response.setResType(ResponseType.RESPONSE_RETURN_FRIENDS_SUCCEED);
                            response.setData(friendList);
                            response.setContent(request.getContent());
                        }
                        //将response对象回复给客户端
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //无异常退出请求
                    case "5": {
                        User user = (User) request.getData();
                        //控制台给一个提示
                        System.out.println(user.getUserId() + "退出了");
                        //将这个客户端的对应线程，从集合删除
                        ManageClientThreads.removeServerConnectClientThread(user.getUserId());
                        //关闭该连接
                        response.setResType(ResponseType.RESPONSE_CLIENT_EXIT);
                        oos.writeObject(response);
                        oos.flush();
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //退出线程
                        ThreadState = false;
                    }
                    break;
                    //判断用户是否在线
                    case "6":{
                        String userId = UserDao.ReturnId((String)request.getData());
                        Boolean b = UserDao.CheckOnline(userId);
                        response.setData(b);
                        response.setResType(ResponseType.RESPONSE_RETURN_ONLINE);
                        //将response对象回复给客户端
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //当接收到的是修改密码请求
                    case "7": {
                        User user = (User) request.getData();
                        if (user.getVcode().equals(ManageEmailVcode.getEmailVcode(user.getemail()))) {
                            //说明验证码匹配正确，允许修改密码
                            ManageEmailVcode.removeEmailVcode(user.getemail());
                            response.setResType(ResponseType.RESPONSE_REFIND_PASSWORD_SUCCEED);
                            //二次加密
                            user.setPasswd(MD5Util.MD5Encode(user.getPasswd(), "utf8"));

                            UserDao.resetPassword(user.getPasswd(), user.getemail());
                            user.setVcode(null);
                        } else {
                            response.setResType(ResponseType.RESPONSE_REFIND_PASSWORD_FAIL);
                        }
                        //将response对象回复给客户端
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //私聊发送消息
                    case "8": {
                        Message message = (Message) request.getData();
                        //要到数据库中根据昵称返回发送者和接收者id
                        String senderId = MessageDao.returnId(message.getSender());
//                    System.out.println("发送者转化得到的id是"+senderId);
                        String getterId = MessageDao.returnId(message.getGetter());
//                    System.out.println("接收者转化得到的id是"+getterId);
                        //将该记录存入数据库
                        if(message.getContent().endsWith("png") ||message.getContent().endsWith("jpg")){
                            message.setContent(ImageSave.ImageSaveUtil(senderId,message.getContent()));
                        }
                        MessageDao.InsertMessage(senderId, getterId, message.getContent(), message.getSendTime());

                        //查询消息接收者是否在线，如果在线，即可返回响应
                        if (ManageClientThreads.Check(getterId)) {
                            System.out.println("申请方用户" + getterId + "在线噢");//测试
                            response.setResType(ResponseType.RESPONSE_COMMON_MES);
                            message.setGetter(MessageDao.returnAvatar(message.getSender()));
                            response.setData(message);
                            ObjectOutputStream oos2 = ManageClientThreads.getServerConnectClientThread(getterId).getOos();
                            oos2.writeObject(response);
                            oos2.flush();
//                        System.out.println(response);
                        }
                    }
                    break;
                    //当接收到的是获取个人资料请求
                    case "9": {
                        User user = (User) request.getData();
                        String idOrEmail = user.getUserId();

                        User user2 = UserDao.findUserInfo(idOrEmail);
                        String url = user2.getAvatar();
                        user2.setSomething(user.getSomething());
                        response.setResType(ResponseType.RESPONSE_PRIVATE_MESSAGE_SUCCESS);
                        response.setData(user2);
                        //将response对象回复给客户端
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //数据库邮箱查重
                    case "10": {
                        User user = (User) request.getData();
                        String email = user.getemail();
                        response.setData(user.getSomething());
                        if (UserDao.emailOnly(email)) {
//                            System.out.println(email + "在数据库中不存在");
                            response.setResType(ResponseType.RESPONSE_EMAIL_NOT_EXIST);
                        } else {
//                            System.out.println(email + "在数据库中存在！");
                            response.setResType(ResponseType.RESPONSE_EMAIL_EXIST);
                        }
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //重新设置密码请求
                    case "11": {
                        User user = (User) request.getData();
                        if (user.getVcode().equals(ManageEmailVcode.getEmailVcode(user.getemail()))) {
//                            System.out.println("修改密码成功");
                            ManageEmailVcode.removeEmailVcode(user.getemail());
                            //说明验证码匹配正确，允许修改密码
                            response.setResType(ResponseType.RESPONSE_RESET_PASSWORD_SUCCEED);
                            //二次加密
                            user.setPasswd(MD5Util.MD5Encode(user.getPasswd(), "utf8"));

                            UserDao.resetPassword(user.getPasswd(), user.getemail());
                            ManageEmailVcode.removeEmailVcode(user.getemail());
                            user.setVcode(null);
                        } else {
//                            System.out.println("修改密码失败");
                            response.setResType(ResponseType.RESPONSE_RESET_PASSWORD_FAIL);
                        }
                        //将response对象回复给客户端
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //修改个人资料
                    case "12": {
                        UserDao.resetPrivateData((User) request.getData());
                    }
                    break;
                    //有用户向其他人发送好友请求
                    case "13": {
                        //把好友请求存到数据库中
                        UserDao.addNewRequest((Message) request.getData());
                    }
                    break;
                    //有用户同意了好友请求
                    case "14": {
                        Message message = (Message) request.getData();
                        String sender = message.getSender();
                        String getter = message.getGetter();
                        //删除数据库中的好友请求
                        UserDao.DeleteAddList(sender, getter);
                        //添加好友关系(在数据库好友表中添加关系)
                        UserDao.AddFriend(sender, getter);
                        //查询申请发送者是否在线，如果在线，即可返回响应
                        if (ManageClientThreads.Check(message.getSender())) {
                            //给申请用户发送回应，对方已添加您为好友
                            System.out.println("申请方用户" + message.getSender() + "在线噢");//测试
                            response.setResType(ResponseType.RESPONSE_REQUEST_AGREE);
                            User user = UserDao.findUserInfo(message.getGetter());
//                            System.out.println("用户" + message.getSender() + "成功添加新好友" + message.getGetter());
//                            System.out.println("新好友的信息是"+user);
                            response.setData(user);
                            ObjectOutputStream oos2 = ManageClientThreads.getServerConnectClientThread(message.getSender()).getOos();
                            oos2.writeObject(response);
                            oos2.flush();
//                            System.out.println(response);
                        }
                    }
                    break;
                    //搜索用户或群组请求
                    case "15": {
                        response.setContent("-1");
                        String objectId = (String) request.getData();
                        String userId = request.getContent();

                        int state = UserDao.searchId(objectId);
                        if (state != 0) {
                            response.setResType(ResponseType.RESPONSE_OBJECT_EXIT);
                            if (state == 1) {
                                //是用户
                                if (UserDao.checkFriend(objectId, userId)) {
                                    //已经是好友了
                                    response.setContent("0");
                                } else {
                                    //可添加好友
                                    response.setContent("1");
                                }
                            }
                            if (state == 2) {
                                //是群组
                                if (UserDao.checkGroup(objectId, userId)) {
                                    //已经加入了此群组
                                    response.setContent("0");
                                } else {
                                    //可添加群组
                                    response.setContent("2");
                                }
                            }
                        } else {
                            //用户或群组不存在
                            response.setResType(ResponseType.RESPONSE_OBJECT_NOT_EXIST);
                        }
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //获取好友申请列表
                    case "16": {
                        String id = (String) request.getData();
                        ArrayList<AddList> addList = UserDao.findAddList(id);
                        response.setResType(ResponseType.RESPONSE_RETURN_ADD_LIST_SUCCEED);
                        response.setData(addList);
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //拒绝添加
                    case "17": {
                        Message message = (Message) request.getData();
                        String sender = message.getSender();
                        String getter = message.getGetter();//删除好友申请
                        UserDao.DeleteAddList(sender, getter);
                    }
                    break;
                    //加载消息列表
                    case "18": {
                        Message message = (Message) request.getData();
                        String user1 = message.getSender();
                        String user2 = UserDao.findId(message.getGetter());
                        ArrayList<MessageList> messageList = UserDao.findMessageList(user1, user2);
                        response.setResType(ResponseType.RESPONSE_RETURN_MESSAGE_LIST);
                        response.setData(messageList);
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //删除好友
                    case "19": {
                        Message message = (Message) request.getData();
                        String user1 = message.getSender();
                        String user2 = message.getGetter();
                        UserDao.DeleteFriend(user1, user2);

                        //查询被删除者是否在线，如果在线，即可返回响应
                        if (ManageClientThreads.Check(user2)) {
                            System.out.println("申请方用户" + user2 + "在线噢");//测试
                            response.setResType(ResponseType.RESPONSE_BE_DELETE);
                            User user = UserDao.findUserInfo(user1);
                            response.setData(user);
                            ObjectOutputStream oos2 = ManageClientThreads.getServerConnectClientThread(user2).getOos();
                            oos2.writeObject(response);
                            oos2.flush();
//                            System.out.println(response);
                        }
                    }
                    break;
                    //设置头像
                    case "20": {
//                    System.out.println("我接收到的消息是"+request);
                        String Id = (String) request.getData();
                        String url = request.getContent();
                        UserDao.SetAvatar(Id, url);
                    }
                    break;
                    //加载群组列表
                    case "21": {
                        String Id = (String) request.getData();
                        ArrayList<ViewList> groupList = GroupDao.findGroupList(Id);
                        response.setResType(ResponseType.RESPONSE_RETURN_GROUP_LIST);
                        response.setData(groupList);
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //加载群资料
                    case "22": {
//                        System.out.println("申请加载页面附带的信息：");
//                        System.out.println(request);
                        String groupName = (String) request.getData();
                        String userId = request.getContent();
                        //操作数据库,得到加载结果
                        GroupInfo groupInfo = GroupDao.LoadGroupInfo(groupName, userId);
                        response.setResType(ResponseType.RESPONSE_GROUP_INFO);
                        response.setData(groupInfo);
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //创建群聊
                    case "23": {
                        GroupInfo groupInfo = (GroupInfo) request.getData();
                        //提取选中的成员昵称集合
                        ArrayList<String> list = (ArrayList<String>) groupInfo.getContent();
                        String groupName = groupInfo.getName();
                        String groupId = groupInfo.getId();
                        String ownName = groupInfo.getOwnNick();
                        //操作数据库,添加群聊以及群成员（以及成员等级）
                        int num = list.size() + 1;
                        //创建群
                        GroupDao.CreatGroup(ownName, groupId, groupName, num);
                        //插入群成员
                        //先加群主
                        GroupDao.AddNewMember(groupId, MessageDao.returnId(ownName), "1");
                        //再加普通群员
                        for (String name : list) {
                            GroupDao.AddNewMember(groupId, MessageDao.returnId(name), "3");
                        }
                        //所有在线选中好友即使添加群组
                        for (String name : list) {
                            String Id = MessageDao.returnId(name);
                            //如果该用户在线
                            if (ManageClientThreads.Check(Id)) {
                                //带着群名字和群号码 发送一个 被添加进了群组 的响应
                                response.setResType(ResponseType.RESPONSE_INVITE_GROUP);
                                response.setData(groupInfo);
                                //获取被邀请人的socket用于传输信息
                                ObjectOutputStream oos2 = ManageClientThreads.getServerConnectClientThread(Id).getOos();
                                oos2.writeObject(response);
                                oos2.flush();
                            }
                        }
                    }
                    break;
                    //设置群头像
                    case "24": {
                        String Id = (String) request.getData();
                        String url = request.getContent();
                        GroupDao.SetGroupAvatar(Id, url);
                    }
                    break;
                    //某用户在群组里发送了消息
                    case "25": {
                        Message message = (Message) request.getData();
                        //要到数据库中根据昵称返回发送者和接收者id
                        String senderId = MessageDao.returnId(message.getSender());
//                    System.out.println("发送者转化得到的id是"+senderId);
                        String groupId = MessageDao.returnGroupId(message.getGetter());
//                    System.out.println("接收者转化得到的id是"+groupId);
                        //将该记录存入数据库
                        if(message.getContent().endsWith("png") ||message.getContent().endsWith("jpg") ){
                            message.setContent(ImageSave.ImageSaveUtil(senderId,message.getContent()));
                        }
                        MessageDao.InsertGroupMessage(senderId, groupId, message.getContent(), message.getSendTime());

                        //查询群组其他成员是否在线，如果在线，即可返回响应
                        //获取群组中除了自己，其他成员的账号id
                        ArrayList<String> groupMember = GroupDao.GetGroupMemberBesidesSelf(groupId, senderId);
                        for (String getterId : groupMember) {
                            if (ManageClientThreads.Check(getterId)) {
                                System.out.println("接收方用户" + getterId + "在线噢");//测试
                                response.setResType(ResponseType.RESPONSE_GROUP_MES);
                                message.setGetter(MessageDao.returnAvatar(senderId));
                                response.setContent(MessageDao.ReturnName(groupId));//群名称
                                response.setData(message);
                                ObjectOutputStream oos2 = ManageClientThreads.getServerConnectClientThread(getterId).getOos();
                                oos2.writeObject(response);
                                oos2.flush();
//                                System.out.println(response);
                            }
                        }

                    }
                    break;
                    //加载群消息
                    case "26": {
                        Message message = (Message) request.getData();
//                        String senderID = message.getSender();
                        String groupID = MessageDao.returnGroupId(message.getGetter());
                        ArrayList<MessageList> messageList = GroupDao.LoadMessageList(groupID);
                        response.setResType(ResponseType.RESPONSE_RETURN_MESSAGE_LIST);
                        response.setData(messageList);
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //登录前退出
                    case "27": {
                        ThreadState = false;
                        System.out.println(socket + "还没登录就退出了");
                    }
                    break;
                    //某人退出群聊或被移除
                    case "28": {
//                        System.out.println(request);
                        String groupId = (String) request.getData();
                        String userId = MessageDao.returnId(request.getContent());
                        GroupDao.DeleteMember(groupId, userId);
                    }
                    break;
                    //改变群员等级
                    case "29": {
                        Message message = (Message) request.getData();
                        String groupId = message.getGetter();
                        String userId = MessageDao.returnId(message.getSender());
                        String grade = message.getContent();
                        GroupDao.UpdateGrade(groupId, userId, grade);
                    }
                    break;
                    //修改群资料
                    case "30": {
                        Message message = (Message) request.getData();
                        String name = message.getSender();
                        String sign = message.getGetter();
                        String groupId = request.getContent();
                        GroupDao.UpdateGroupData(name, sign, groupId);
                    }
                    break;
                    //解散群组
                    case "31": {
                        String groupId = (String) request.getData();
                        GroupDao.DeleteMember(groupId);
                        GroupDao.DeleteMessages(groupId);
                        GroupDao.DeleteGroup(groupId);
                    }
                    break;
                    //加载群组最基本的信息
                    case "32": {
                        String groupId = (String) request.getData();
                        GroupInfo groupInfo = GroupDao.LoadGroupBasicInfo(groupId);
                        response.setResType(ResponseType.RESPONSE_BASIC_GROUP_INFO);
                        response.setData(groupInfo);
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //加群申请
                    case "33": {
                        GroupDao.addNewRequest((Message) request.getData());
                    }
                    break;
                    //加载群组申请列表
                    case "34": {
                        String userId = (String) request.getData();
                        ArrayList<AddList> addList = GroupDao.FindAddList(userId);
                        response.setResType(ResponseType.RESPONSE_RETURN_GROUP_ADD_LIST);
                        response.setData(addList);
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //拒绝用户加入群组
                    case "35": {
                        String userId = MessageDao.returnId((String) request.getData());
                        String groupId = MessageDao.returnGroupId(request.getContent());
                        //删除数据库中的加群请求
                        GroupDao.DeleteGroupAddList(userId, groupId);
                    }
                    break;
                    //同意用户加入群组
                    case "36": {
                        String userId = MessageDao.returnId((String) request.getData());
                        String groupId = MessageDao.returnGroupId(request.getContent());
                        //删除数据库中的加群请求
                        GroupDao.DeleteGroupAddList(userId, groupId);
                        //添加好友关系(在数据库好友表中添加关系)
                        GroupDao.AddNewMember(groupId, userId, "3");
                    }
                    break;
                    //群主转让该群
                    case "37": {
                        Message message = (Message) request.getData();
                        String groupId = message.getContent();
                        String newOwnId = MessageDao.returnId(message.getGetter());
                        String oldOwnId = MessageDao.returnId(message.getSender());
                        GroupDao.UpdateGrade(groupId, oldOwnId, "2");
                        GroupDao.UpdateGrade(groupId, newOwnId, "1");
                        GroupDao.UpdateGroupOwn(groupId,newOwnId);

                    }
                    break;
                    //查询群成员在群组中的身份
                    case "38":{
                        String grade = GroupDao.CheckGrade((String)request.getData(),request.getContent());
//                        System.out.println("得到我的身份是"+grade);
                        response.setContent(grade);
                        response.setResType(ResponseType.RESPONSE_RETURN_GRADE);
                        oos.writeObject(response);
                        oos.flush();

                    }
                    break;
                    //添加常用语
                    case "39":{
                        String userId = (String) request.getData();
                        String phrases = request.getContent();
                        MessageDao.InsertCommonPhrases(userId,phrases);
                    }
                    break;
                    //加载常用语
                    case "40":{
                        String userId =(String) request.getData();
                        ArrayList<String> list = MessageDao.LoadCommonPhrases(userId);
                        response.setData(list);
                        response.setResType(ResponseType.LOAD_COMMON_PHRASES);
                        oos.writeObject(response);
                        oos.flush();
                    }
                    break;
                    //增加在线状态
                    case "41":{
                        String userId = UserDao.ReturnId((String)request.getData());
                        UserDao.AddOnlineUser(userId);
                    }
                    break;
                    //删除在线状态
                    case "42":{
                        String userId = (String) request.getData();
                        UserDao.DeleteOnlineUser(userId);
                    }
                    break;
                    //昵称查重
                    case "43":{
                        String name = (String) request.getData();
                        Boolean b = UserDao.CheckName(name);
                        System.out.println("查询结果是"+b);
                        response.setData(b);
                        response.setResType(ResponseType.RESPONSE_CHECK_NAME);
                        oos.writeObject(response);
                        oos.flush();
                    }break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
