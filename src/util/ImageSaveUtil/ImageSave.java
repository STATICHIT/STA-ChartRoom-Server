package util.ImageSaveUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSave {

    public static String ImageSaveUtil(String userId,String url1) throws IOException {
        String newUrl = null;
//        File result = new File("D:\\桌面\\劈各却\\终点\\001.png");//需要复制到的路径，以及图片的新命名+格式
        long num = ImageCount.picCount();
        String urlTo = null;
        if(url1.endsWith("png")){
            urlTo = "D:\\桌面\\劈各却\\UserMessageImage\\\\"+userId+"-"+num+".png";
        }else if(url1.endsWith("jpg")){
            urlTo = "D:\\桌面\\劈各却\\UserMessageImage\\\\"+userId+"-"+num+".jpg";
        }

        File result = new File(urlTo);//需要复制到的路径，以及图片的新命名+格式
//        FileInputStream input = new FileInputStream("D:\\桌面\\劈各却\\000.png");//需要复制的原图的路径+图片名+ .png(这是该图片的格式)
        FileInputStream input = new FileInputStream(url1.substring(5));//需要复制的原图的路径+图片名+ .png(这是该图片的格式)
        FileOutputStream out = new FileOutputStream(result);
        byte[] buffer = new byte[100];//一个容量，相当于打水的桶，可以自定义大小
        int hasRead = 0;
        while ((hasRead = input.read(buffer)) > 0) {
            out.write(buffer, 0, hasRead);//0：表示每次从0开始
        }
        System.out.println(result.getAbsolutePath());
        input.close();//关闭
        out.close();
        return "file:"+result.getAbsolutePath();
    }

    public static void main(String[] args) throws IOException {
        ImageSave.ImageSaveUtil("100001","D:\\桌面\\劈各却\\4948.png");
    }
}
