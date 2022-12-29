package util.sendemail;

import java.util.Random;

public class VerifiCode {

    private String C = "";

    public VerifiCode() {
    }

    public String getC() {
        String code = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        for (int i = 1; i <= 6; i++) {
            Random r = new Random();
            int rr = r.nextInt(50);
            this.C = this.C + code.charAt(rr);
        }
        return C;
    }
}
