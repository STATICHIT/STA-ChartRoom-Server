package chat;

import java.io.Serializable;

public class MessageList implements Serializable {
    private static final long serialVersionUID = 7L;
    private String id;//昵称
    private String image;//头像
    private String message;//消息内容
    private String time;//发送时间

    public MessageList() {
    }

    public MessageList(String id, String message, String time) {
        this.id = id;
        this.message = message;
        this.time = time;
    }

    public MessageList(String id, String message, String time, String image) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.image = image;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "MessageList{" +
                "id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
