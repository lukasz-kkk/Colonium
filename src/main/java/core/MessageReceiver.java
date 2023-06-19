package core;

import Screens.GameScreen;

import static Utils.Definitions.*;

public class MessageReceiver {
    public int handleResponse() {
        String type;
        try{
            type = Client.jsonResponse.get("type").toString();
        } catch (NullPointerException e){
            return PACKET_LOSS;
        }

        switch (type) {
            case "attack":
                handleAttack();
                return SUCCESS;
            case "map":
                handleMap();
                return SUCCESS;
            case "lobbies":
                handleLobbies();
                return SUCCESS;
            case "success":
                return SUCCESS;
            case "start_map":
                Client.startMapFlag = 1;
                return SUCCESS;
            default:
                return UNKNOWN_TYPE_ERROR;
        }
    }

    private void handleAttack() {
        String source = Client.jsonResponse.get("src").toString();
        int src = Integer.parseInt(source);
        String destination = Client.jsonResponse.get("dst").toString();
        int dst = Integer.parseInt(destination);
        if (GameScreen.unitHandler != null) {
            GameScreen.unitHandler.sendUnits(src, dst);
        }
    }

    private void handleMap() {
        MessageUtility.jsonMapMessageDecoder(Client.jsonResponse);
    }
    private void handleLobbies(){
        if(Client.getLobbiesInfo == 1){
            Client.lobbies.clear();
            Client.lobbies = MessageUtility.jsonDecodeLobbies(Client.jsonResponse.toString());
            if(Client.currentLobby != null)
                Client.players = MessageUtility.jsonDecodePlayers(Client.jsonResponse.toString());
            Client.getLobbiesInfo = 0;
        }
    }
}
