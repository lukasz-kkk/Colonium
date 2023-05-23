package core;

public class MessageUtility {
    public static String createMoveJSON(int src, int dst) {
        return "{\"type\":\"move\",\"src\":" + src + ",\"dst\":" + dst + "}";
    }
}
