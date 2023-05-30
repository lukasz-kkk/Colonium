package core;

import objects.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageUtility {
    public static String createMoveJSON(int src, int dst) {
        JSONObject obj = new JSONObject();

        obj.put("type", "move");
        obj.put("src", src);
        obj.put("dst", dst);


        return obj.toString();
    }
    public static String createAttackJSON(int src, int dst){
        JSONObject obj = new JSONObject();
        obj.put("type", "attack");
        obj.put("src", src);
        obj.put("dst", dst);

        return obj.toString();
    }

    public static String createMapUpdateRequest(){
        JSONObject obj = new JSONObject();
        obj.put("type", "map");
        return obj.toString();
    }

    public static JSONObject reciveJSON(String recived) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(recived);
    }

    public static void jsonMapMessageDecoder(JSONObject json){
        if(Map.provinces == null) return;
        long temp = (Long) json.get("temp");
        JSONArray provinces = (JSONArray) json.get("provinces");
        long tickRate = (Long) json.get("tickrate");

        for (Object provinceObj : provinces) {
            JSONObject province = (JSONObject) provinceObj;
            long owner = (Long) province.get("owner");
            long id = (Long) province.get("id");
            long income = (Long) province.get("income");
            long manpower = (Long) province.get("manpower");
            long army = (Long) province.get("army");
            if(Map.provinces[(int)id] == null) return;

            Map.provinces[(int)id].owner = (int)owner;
            Map.provinces[(int)id].setValue((int)army);

        }
    }


}
