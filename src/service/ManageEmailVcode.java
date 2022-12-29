package service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 专门用于保存用户和对应生成的二维码
 * 在验证完要删除该验证码
 */
public class ManageEmailVcode {
    private static final ConcurrentHashMap<String, String> hm2 = new ConcurrentHashMap<>();

    //添加线程到hm集合的方法
    public static void AddEmailVcode(String email, String vcode) {
        hm2.put(email, vcode);
    }

    //从集合中移除某个记录
    public static void removeEmailVcode(String email) {
        hm2.remove(email);
    }

    //根据Email返回验证码
    public static String getEmailVcode(String Email) {
        return hm2.get(Email);
    }
}
