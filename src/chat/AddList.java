package chat;

import java.io.Serializable;

public class AddList implements Serializable {
    private static final long serialVersionUID = 6L;
    String nickName;//昵称
    String id;//账号
    String image;//头像
    String sex;//性别
    String age;//年龄
    String text;//备注
    String time;//时间
    String content;//其他内容


    public AddList(String nickName, String id, String image, String sex, String age, String text, String time) {
        this.nickName = nickName;
        this.id = id;
        this.image = image;
        this.sex = sex;
        this.age = age;
        this.text = text;
        this.time = time;
    }

    public AddList(String nickName, String id, String image, String sex, String age, String text, String time, String content) {
        this.nickName = nickName;
        this.id = id;
        this.image = image;
        this.sex = sex;
        this.age = age;
        this.text = text;
        this.time = time;
        this.content = content;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AddList{" +
                "nickName='" + nickName + '\'' +
                ", id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", text='" + text + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
