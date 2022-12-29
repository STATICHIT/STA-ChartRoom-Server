package chat;

import java.io.Serializable;

/**
 * 泛型反馈类
 *
 * @param <T>
 */
public class Response<T> implements Serializable{
    private static final long serialVersionUID = 3L;
    //响应类型
    private String ResType;
    //响应回传的内容
    private String content;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getResType() {
        return ResType;
    }

    public void setResType(String resType) {
        ResType = resType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Response{" +
                "ResType='" + ResType + '\'' +
                ", content='" + content + '\'' +
                ", data=" + data +
                '}';
    }
}
