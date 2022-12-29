package chat;

import java.io.Serializable;

/**
 * 泛型 请求类
 *
 * @param <T>
 */
public class Request<T> implements Serializable {
    private static final long serialVersionUID = 2L;

    //发送时间
    private String content;

    //消息类型[可以在接口定义消息类型]
    private String mesType;

    //传输所带数据
    private T data;

    public Request() {
    }

    public Request(String mesType, T data) {
        this.mesType = mesType;
        this.data = data;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String concent) {
        this.content = concent;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Request{" +
                "concent='" + content + '\'' +
                ", mesType='" + mesType + '\'' +
                ", data=" + data +
                '}';
    }
}