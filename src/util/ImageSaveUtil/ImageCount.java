package util.ImageSaveUtil;

import java.io.File;

public class ImageCount {
    public static long picCount() {
        File file = new File("D:\\桌面\\劈各却\\UserMessageImage");// 图片存放路径
        File[] list = file.listFiles();
        assert list != null;
        return list.length;
    }
}
