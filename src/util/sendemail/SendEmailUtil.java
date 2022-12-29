package util.sendemail;

import org.apache.commons.mail.HtmlEmail;

import static util.snowID.TestSnowFlake.getOneID;

public class SendEmailUtil {

    public static boolean sendEmail(String acount, String code) {
        try {
            //创建网页邮箱对象
            HtmlEmail email = new HtmlEmail();

            //基本设置
            email.setDebug(false);

            //设置为QQ邮箱作为发送主邮箱
            email.setHostName("SMTP.qq.com");
            email.setSmtpPort(587);

            //qq邮箱的验证信息
            email.setAuthentication("2394412110@qq.com", "bthyktrteuaidjdj");

            //设置邮件发送人
            email.setFrom("2394412110@qq.com");

            //设置邮件接收人
            email.addTo(acount);

            //设置发送的内容
//            email.setMsg("欢迎注册STA聊天室账号！\n\\`v'/\n您的验证码是" + code + "，请不要把验证码泄露给其他人哦\n");
            email.setMsg("TEXT\n");

            //设置邮箱标题
//            email.setSubject("This is your captcha.");
            email.setSubject("AAA");

            //执行邮件发送
            email.send();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        VerifiCode c = new VerifiCode();
        String code = c.getC();
        boolean b = sendEmail("2394412110@qq.com", code);
        //1158965969@qq.com 天天的邮箱
        //843497509@qq.com hyx的邮箱
        if (b) {
            System.out.println("发送成功");
            String id;
            id = getOneID();
            System.out.println("您生成的账号是：" + id);
        } else {
            System.out.println("发送失败");
        }
        System.out.println("发送" + (b ? "成功" : "失败"));
    }
}
