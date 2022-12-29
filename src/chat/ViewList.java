package chat;

import java.io.Serializable;

public class ViewList implements Serializable {
    private static final long serialVersionUID = 5L;
    private String id;//昵称
    private String image;//头像
    private String sign;//签名
    private Boolean check;//选择

    public ViewList() {
    }

    public ViewList(String id, String image) {
        this.id = id;
        this.image = image;
    }

    public ViewList(String id, String sign, String image) {
        this.id = id;
        this.image = image;
        this.sign = sign;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean b) {
        this.check = b;
    }

    @Override
    public String toString() {
        return "ViewList{" +
                "id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", sign='" + sign + '\'' +
                ", check=" + check +
                '}';
    }
}
