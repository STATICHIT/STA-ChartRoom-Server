package chat;

import java.io.Serializable;

/**
 * 用户类
 */
public class User implements Serializable{
    private static final long serialVersionUID = 4L;
    private String userId;//用户id
    private String avatar;//头像
    private String nickName;//昵称
    private String passwd;//密码
    private String email;//邮箱
    private String vcode;//验证码
    private String requestType;//请求类型
    private String sex;//性别
    private String birth;//生日
    private String sign;//个性签名
    private String Something;//某东西

    public User() {
    }

    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public User(String nickName, String passwd, String vcode) {
        this.nickName = nickName;
        this.passwd = passwd;
        this.vcode = vcode;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }


    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSomething() {
        return Something;
    }

    public void setSomething(String something) {
        Something = something;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickName='" + nickName + '\'' +
                ", passwd='" + passwd + '\'' +
                ", email='" + email + '\'' +
                ", vcode='" + vcode + '\'' +
                ", requestType='" + requestType + '\'' +
                ", sex='" + sex + '\'' +
                ", birth='" + birth + '\'' +
                ", sign='" + sign + '\'' +
                ", Something='" + Something + '\'' +
                '}';
    }
}
