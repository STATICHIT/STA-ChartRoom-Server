package chat;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupInfo<T> implements Serializable {
    private static final long serialVersionUID = 8L;
    private String avatar;//群头像
    private String id;//群id
    private String name;//群名字
    private String sign;//群介绍
    private String num;//群人数
    private String ownNick;//群主昵称
    private String ownImage;//群主头像
    private ArrayList<ViewList> OtherList;//普通成员列表
    private ArrayList<ViewList> manageList;//管理员列表
    private T content;//其他数据

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setOwnNick(String ownNick) {
        this.ownNick = ownNick;
    }

    public String getOwnNick() {
        return ownNick;
    }

    public String getOwnImage() {
        return ownImage;
    }

    public void setOwnImage(String ownImage) {
        this.ownImage = ownImage;
    }

    public ArrayList<ViewList> getManageList() {
        return manageList;
    }

    public void setManageList(ArrayList<ViewList> manageList) {
        this.manageList = manageList;
    }

    public ArrayList<ViewList> getOtherList() {
        return OtherList;
    }

    public void setOtherList(ArrayList<ViewList> otherList) {
        this.OtherList = otherList;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "avatar='" + avatar + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sign='" + sign + '\'' +
                ", num='" + num + '\'' +
                ", ownNick='" + ownNick + '\'' +
                ", ownImage='" + ownImage + '\'' +
                ", OtherList=" + OtherList +
                ", manageList=" + manageList +
                ", content=" + content +
                '}';
    }
}
