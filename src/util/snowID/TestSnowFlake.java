package util.snowID;

public class TestSnowFlake {
    public TestSnowFlake() {
    }

    /**
     * 生成一个账号
     */
    public static String getOneID() {
        SnowFlake idWoker = new SnowFlake(1L, 1L);
        String id;
        id = String.valueOf(idWoker.nextId());
        return id;
    }

    public static void main(String[] args) {
        System.out.println(getOneID());
    }
}
