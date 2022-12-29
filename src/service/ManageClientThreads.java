package service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于管理”和客户端通信的线程“
 * 利用ConcurrentHashMap管理
 * ConcurrentHashMap能够解决线程安全问题
 */
public class ManageClientThreads {
    //ConcurrentHashMap可以处理并发的集合，没有线程安全问题
    private static final ConcurrentHashMap<String, ServerConnectClientThread> hmm = new ConcurrentHashMap<>();

    //添加线程到hm集合的方法
    public static void addClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hmm.put(userId, serverConnectClientThread);
    }

    //从集合中移除某个线程对象
    public static void removeServerConnectClientThread(String userId) {
        hmm.remove(userId);
    }

    //根据userId返回对应线程
    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return hmm.get(userId);
    }

    //查询某用户的socket是否存在
    public static boolean Check(String Id) {
        boolean b = false;
        for (String s : hmm.keySet()) {
            if (s.equals(Id)) {
                b = true;
                break;
            }
        }
        return b;
    }
}