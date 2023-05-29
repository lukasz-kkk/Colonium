package core;

import org.json.simple.JSONObject;

public class MessageUtility {
    public static String createMoveJSON(int src, int dst) {
        JSONObject obj = new JSONObject();

        obj.put("type", "move");
        obj.put("src", src);
        obj.put("dst", dst);


        return obj.toJSONString();
    }
    public static String createAttackJSON(int src, int dst){
        JSONObject obj = new JSONObject();
        obj.put("type", "attack");
        obj.put("src", src);
        obj.put("dst", dst);

        return obj.toString();
    }
}
