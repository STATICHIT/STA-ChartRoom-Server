package chat;

/**
 * 表示反馈类型
 */
public interface ResponseType {
    //1.在接口中定义了一些常量
    //2，不同的常量的值，表示不同的消息类型
    String RESPONSE_LOGIN_SUCCEED = "1";//表示登录成功
    String RESPONSE_LOGIN_FAIL = "2";//表示登录失败
    String RESPONSE_ENROLL_SUCCEED = "3";//表示注册成功
    String RESPONSE_ENROLL_FAIL = "4";//表示注册失败
    String RESPONSE_COMMON_MES = "5";//普通消息包
    String RESPONSE_RETURN_FRIENDS_SUCCEED = "6";//返回在线用户列表成功
    String RESPONSE_REFIND_PASSWORD_SUCCEED = "7";//表示找回密码成功
    String RESPONSE_REFIND_PASSWORD_FAIL = "8";//表示找回密码失败
    String RESPONSE_PRIVATE_MESSAGE_SUCCESS = "9";//请求返回个人信息
    String RESPONSE_RETURN_ONLINE = "10";//返回在线状态
    String RESPONSE_RETURN_GRADE = "11";//返回身份查询结果
    String LOAD_COMMON_PHRASES = "12";//返回自定义常用语
    String RESPONSE_RETURN_FRIENDS_FAIL = "13";//返回在线用户列失败
    String RESPONSE_EMAIL_EXIST = "14";//表示邮箱存在
    String RESPONSE_EMAIL_NOT_EXIST = "15";//表示邮箱不存在，可用
    String RESPONSE_CLIENT_EXIT = "16";//表示服务端已退出
    String RESPONSE_RESET_PASSWORD_SUCCEED = "17";//表示修改密码成功
    String RESPONSE_RESET_PASSWORD_FAIL = "18";//表示修改密码失败
    String RESPONSE_OBJECT_EXIT = "19";//查询对象存在
    String RESPONSE_OBJECT_NOT_EXIST = "20";//查询对象不存在
    String RESPONSE_RETURN_ADD_LIST_SUCCEED = "21";//返回申请列表成功
    String RESPONSE_REQUEST_AGREE = "23";//对方已同意加您为好友
    String RESPONSE_RETURN_MESSAGE_LIST= "24";//返回聊天消息列表
    String RESPONSE_BE_DELETE = "25";//被删除的响应
    String RESPONSE_RETURN_GROUP_LIST = "26";//返回群组列表
    String RESPONSE_INVITE_GROUP = "27";//被添加进了群组响应
    String RESPONSE_GROUP_INFO = "28";//返回群组资料
    String RESPONSE_GROUP_MES = "29";//群消息
    String RESPONSE_BASIC_GROUP_INFO = "30";//返回群组最基本的信息
    String RESPONSE_RETURN_GROUP_ADD_LIST = "31";//返回群组申请列表
    String RESPONSE_CHECK_NAME = "32";//昵称查重结果
}
