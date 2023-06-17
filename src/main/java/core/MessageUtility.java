package core;

import objects.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class MessageUtility {
    public static String setNameJSON(String userName){
        JSONObject obj = new JSONObject();

        obj.put("type", "set_name");
        obj.put("value", userName);

        return obj.toJSONString();
    }
    public static String createLobbyJSON(String name){
        JSONObject obj = new JSONObject();

        obj.put("type", "create_lobby");
        obj.put("lobby_name", name);

        return obj.toString();
    }
    public static String joinLobbyJSON(String name){
        JSONObject obj = new JSONObject();

        obj.put("type", "join_lobby");
        obj.put("lobby_name", name);

        return obj.toString();
    }
    public static String leaveLobbyJSON(){
        JSONObject obj = new JSONObject();

        obj.put("type", "leave_lobby");

        return obj.toString();
    }
    public static String resetMapJSON(String name){
        JSONObject obj = new JSONObject();

        obj.put("type", "reset_map");
        obj.put("lobby_name", name);

        return obj.toString();
    }
    public static String startMapJSON(String lobbyName){
        JSONObject obj = new JSONObject();

        obj.put("type", "start_map");
        obj.put("lobby_name", lobbyName);

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
    public static String createLobbiesRequest(){
        JSONObject obj = new JSONObject();
        obj.put("type", "list_lobbies");
        return obj.toString();
    }

    public static JSONObject reciveJSON(String recived) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(recived);
    }

    public static String createUpgradeRequest(String lobbyName, int provinceID, String upgrade, int cost){
        JSONObject obj = new JSONObject();
        obj.put("type", "upgrade");
        obj.put("lobbyname", lobbyName);
        obj.put("province_id", provinceID);
        obj.put("upgrade", upgrade);
        obj.put("cost", cost);
        return obj.toString();
    }

    public static void jsonMapMessageDecoder(JSONObject json){
        if(Map.provinces == null) return;
        long temp = (Long) json.get("temp");
        JSONObject gold = (JSONObject) json.get("playersgold");
        Client.gold = (Double) gold.get(Client.clientName);

        JSONArray provinces = (JSONArray) json.get("provinces");
        long tickRate = (Long) json.get("tickrate");

        for (Object provinceObj : provinces) {
            JSONObject province = (JSONObject) provinceObj;
            String owner = (String) province.get("owner");
            long id = (Long) province.get("id");
            long income = (Long) province.get("income");
            long manpower = (Long) province.get("manpower");
            long army = (Long) province.get("army");
            if(Map.provinces[(int)id] == null) return;
            Map.provinces[(int)id].owner = owner;
            Map.provinces[(int)id].setValue((int)army);
        }
        System.out.println(Client.gold);
    }

    public static List<String> jsonDecodeLobbies(String json) {
        List<String> lobbyInfoList = new ArrayList<>();

        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(json);
            JSONArray lobbiesArray = (JSONArray) jsonObject.get("lobbies");

            for (Object lobby : lobbiesArray) {
                JSONObject lobbyObject = (JSONObject) lobby;
                String lobbyName = (String) lobbyObject.get("name");
                JSONArray playersArray = (JSONArray) lobbyObject.get("players");
                int playerCount = playersArray.size();

                String lobbyInfo = lobbyName + " - " + playerCount + " players";
                lobbyInfoList.add(lobbyInfo);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return lobbyInfoList;
    }

    public static List<String> jsonDecodePlayers(String json) {
        List<String> playersInfoList = new ArrayList<>();

        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(json);
            JSONArray lobbiesArray = (JSONArray) jsonObject.get("lobbies");

            for (Object lobby : lobbiesArray) {
                JSONObject lobbyObject = (JSONObject) lobby;
                String lobbyName = (String) lobbyObject.get("name");
                if(lobbyName.equals(Client.currentLobby)){
                    JSONArray playersArray = (JSONArray) lobbyObject.get("players");
                    for(Object player : playersArray){
                        playersInfoList.add(player.toString());
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return playersInfoList;
    }

}